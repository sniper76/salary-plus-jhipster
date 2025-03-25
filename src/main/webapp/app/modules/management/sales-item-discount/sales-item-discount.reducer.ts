import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { defaultValue, IShopSalesItemDiscount } from 'app/shared/model/shopSalesItemDiscount.model';

const initialState = {
  loading: false,
  errorMessage: null,
  salesItemDiscount: defaultValue,
  salesItemDiscountsForPage: [] as ReadonlyArray<IShopSalesItemDiscount>,
  updating: false,
  updateSuccess: false,
  totalItems: 0,
};

const apiUrl = 'api/shops';

// Async Actions

export const getPageShopSalesItemDiscounts = createAsyncThunk(
  'management/sales_item_discounts_page',
  async ({ id, page, size, sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}/${id}/sales-item-discounts${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
    console.warn('requestUrl', requestUrl);
    return axios.get<IShopSalesItemDiscount[]>(requestUrl);
  },
);

export const getShopSalesItemDiscount = createAsyncThunk('management/sales_item_discount', async ({ shopId, salesItemDiscountId }: any) => {
  const requestUrl = `${apiUrl}/${shopId}/sales-item-discounts/${salesItemDiscountId}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<IShopSalesItemDiscount>(requestUrl);
});

export const createSalesItemDiscount = createAsyncThunk(
  'management/create_sales_item_discount',
  async (user: IShopSalesItemDiscount, thunkAPI) => {
    const requestUrl = `${apiUrl}/${user.shopId}/sales-item-discounts`;
    console.warn('requestUrl', requestUrl);
    const result = await axios.post<IShopSalesItemDiscount>(requestUrl, user);
    thunkAPI.dispatch(getPageShopSalesItemDiscounts({ id: user.shopId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateSalesItemDiscount = createAsyncThunk(
  'management/update_sales_item_discount',
  async (user: IShopSalesItemDiscount, thunkAPI) => {
    const requestUrl = `${apiUrl}/${user.shopId}/sales-item-discounts`;
    const result = await axios.put<IShopSalesItemDiscount>(requestUrl, user);
    thunkAPI.dispatch(getPageShopSalesItemDiscounts({ id: user.shopId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteSalesItemDiscount = createAsyncThunk(
  'management/delete_sales_item_discount',
  async ({ shopId, salesItemDiscountId }: any, thunkAPI) => {
    const requestUrl = `${apiUrl}/${shopId}/sales-item-discounts/${salesItemDiscountId}`;
    const result = await axios.delete<IShopSalesItemDiscount>(requestUrl);
    thunkAPI.dispatch(getPageShopSalesItemDiscounts({ id: shopId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export type SalesItemDiscountState = Readonly<typeof initialState>;

export const SalesItemDiscountSlice = createSlice({
  name: 'salesItemDiscounts',
  initialState: initialState as SalesItemDiscountState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getShopSalesItemDiscount.fulfilled, (state, action) => {
        state.loading = false;
        state.salesItemDiscount = action.payload.data;
      })
      .addCase(deleteSalesItemDiscount.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.salesItemDiscount = defaultValue;
      })
      .addMatcher(isFulfilled(getPageShopSalesItemDiscounts), (state, action) => {
        state.loading = false;
        state.salesItemDiscountsForPage = action.payload.data;
        console.warn('action.payload.data', action.payload.data);
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
      })
      .addMatcher(isFulfilled(createSalesItemDiscount, updateSalesItemDiscount), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.salesItemDiscount = action.payload.data;
      })
      .addMatcher(isPending(getPageShopSalesItemDiscounts, getShopSalesItemDiscount), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createSalesItemDiscount, updateSalesItemDiscount, deleteSalesItemDiscount), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      })
      .addMatcher(isRejected(getPageShopSalesItemDiscounts, getShopSalesItemDiscount), (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const { reset } = SalesItemDiscountSlice.actions;

// Reducer
export default SalesItemDiscountSlice.reducer;
