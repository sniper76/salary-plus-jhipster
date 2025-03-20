import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IShopSalesItem, defaultValue } from 'app/shared/model/shopSalesItem.model';

const initialState = {
  loading: false,
  errorMessage: null,
  salesItem: defaultValue,
  salesItemsForPage: [] as ReadonlyArray<IShopSalesItem>,
  updating: false,
  updateSuccess: false,
  totalItems: 0,
};

const apiUrl = 'api/shops/';

// Async Actions

export const getPageShopSalesItems = createAsyncThunk('management/sales_items_page', async ({ id, page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}${id}/sales-items${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<IShopSalesItem[]>(requestUrl);
});

export const getShopSalesItem = createAsyncThunk('management/sales_item', async ({ shopId, salesItemId }: any) => {
  const requestUrl = `${apiUrl}${shopId}/sales-items/${salesItemId}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<IShopSalesItem>(requestUrl);
});

export const createSalesItem = createAsyncThunk(
  'management/create_sales_item',
  async (user: IShopSalesItem, thunkAPI) => {
    const result = await axios.post<IShopSalesItem>(apiUrl, user);
    thunkAPI.dispatch(getPageShopSalesItems({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateSalesItem = createAsyncThunk(
  'management/update_sales_item',
  async (user: IShopSalesItem, thunkAPI) => {
    const result = await axios.put<IShopSalesItem>(apiUrl, user);
    thunkAPI.dispatch(getPageShopSalesItems({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteSalesItem = createAsyncThunk(
  'management/delete_sales_item',
  async (id: string, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IShopSalesItem>(requestUrl);
    thunkAPI.dispatch(getPageShopSalesItems({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export type SalesItemState = Readonly<typeof initialState>;

export const SalesItemSlice = createSlice({
  name: 'salesItems',
  initialState: initialState as SalesItemState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getShopSalesItem.fulfilled, (state, action) => {
        state.loading = false;
        state.salesItem = action.payload.data;
      })
      .addCase(deleteSalesItem.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.salesItem = defaultValue;
      })
      .addMatcher(isFulfilled(getPageShopSalesItems), (state, action) => {
        state.loading = false;
        state.salesItemsForPage = action.payload.data;
        console.warn('action.payload.data', action.payload.data);
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
      })
      .addMatcher(isFulfilled(createSalesItem, updateSalesItem), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.salesItem = action.payload.data;
      })
      .addMatcher(isPending(getPageShopSalesItems, getShopSalesItem), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createSalesItem, updateSalesItem, deleteSalesItem), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      })
      .addMatcher(isRejected(getPageShopSalesItems, getShopSalesItem), (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const { reset } = SalesItemSlice.actions;

// Reducer
export default SalesItemSlice.reducer;
