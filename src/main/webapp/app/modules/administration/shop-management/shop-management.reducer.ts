import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { defaultValue, IShop } from 'app/shared/model/shop.model';
import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  shops: [] as ReadonlyArray<IShop>,
  authorities: [] as any[],
  shop: defaultValue,
  updating: false,
  updateSuccess: false,
  totalItems: 0,
};

const adminUrl = 'api/admin/shops';

// Async Actions

export const getShopsAsAdmin = createAsyncThunk('shopManagement/fetch_shops_as_admin', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${adminUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<IShop[]>(requestUrl);
});

export const getShop = createAsyncThunk(
  'shopManagement/fetch_shop',
  async (id: string) => {
    const requestUrl = `${adminUrl}/${id}`;
    return axios.get<IShop>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createShop = createAsyncThunk(
  'shopManagement/create_shop',
  async (shop: IShop, thunkAPI) => {
    const result = await axios.post<IShop>(adminUrl, shop);
    thunkAPI.dispatch(getShopsAsAdmin({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateShop = createAsyncThunk(
  'shopManagement/update_shop',
  async (shop: IShop, thunkAPI) => {
    const result = await axios.put<IShop>(adminUrl, shop);
    thunkAPI.dispatch(getShopsAsAdmin({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteShop = createAsyncThunk(
  'shopManagement/delete_shop',
  async (id: string, thunkAPI) => {
    const requestUrl = `${adminUrl}/${id}`;
    const result = await axios.delete<IShop>(requestUrl);
    thunkAPI.dispatch(getShopsAsAdmin({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export type ShopManagementState = Readonly<typeof initialState>;

export const ShopManagementSlice = createSlice({
  name: 'shopManagement',
  initialState: initialState as ShopManagementState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getShop.fulfilled, (state, action) => {
        state.loading = false;
        state.shop = action.payload.data;
      })
      .addCase(deleteShop.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.shop = defaultValue;
      })
      .addMatcher(isFulfilled(getShopsAsAdmin), (state, action) => {
        state.loading = false;
        state.shops = action.payload.data;
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
      })
      .addMatcher(isFulfilled(createShop, updateShop), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.shop = action.payload.data;
      })
      .addMatcher(isPending(getShopsAsAdmin, getShop), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createShop, updateShop, deleteShop), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      })
      .addMatcher(isRejected(getShopsAsAdmin, getShop, createShop, updateShop, deleteShop), (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const { reset } = ShopManagementSlice.actions;

// Reducer
export default ShopManagementSlice.reducer;
