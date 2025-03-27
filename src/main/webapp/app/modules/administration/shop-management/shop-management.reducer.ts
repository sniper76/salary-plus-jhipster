import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { defaultValue, IShop } from 'app/shared/model/shop.model';
import { IShopModel } from 'app/shared/model/shopModel.model';
import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { getShopModels } from 'app/modules/order/order.reducer';

const initialState = {
  loading: false,
  errorMessage: null,
  shopsForPage: [] as ReadonlyArray<IShop>,
  shops: [] as ReadonlyArray<IShop>,
  authorities: [] as any[],
  shop: defaultValue,
  updating: false,
  updateSuccess: false,
  totalItems: 0,
};

const adminUrl = 'api/admin/shops';

// Async Actions

export const getShopsAsAdminForPage = createAsyncThunk(
  'shopManagement/fetch_shops_as_admin_for_admin',
  async ({ page, size, sort }: IQueryParams) => {
    const requestUrl = `${adminUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
    return axios.get<IShop[]>(requestUrl);
  },
);

export const getShopsAsAdmin = createAsyncThunk('shopManagement/fetch_shops_as_admin', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${adminUrl}/all${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
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
    thunkAPI.dispatch(getShopsAsAdminForPage({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const createShopModels = createAsyncThunk(
  'shopManagement/create_shop_models',
  async (shop: IShopModel, thunkAPI) => {
    const requestUrl = `api/shops/${shop.shopId}/models`;
    const result = await axios.post<IShopModel>(requestUrl, shop);
    thunkAPI.dispatch(getShopModels({ shopId: shop.shopId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateShop = createAsyncThunk(
  'shopManagement/update_shop',
  async (shop: IShop, thunkAPI) => {
    const result = await axios.put<IShop>(adminUrl, shop);
    thunkAPI.dispatch(getShopsAsAdminForPage({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteShop = createAsyncThunk(
  'shopManagement/delete_shop',
  async (id: string, thunkAPI) => {
    const requestUrl = `${adminUrl}/${id}`;
    const result = await axios.delete<IShop>(requestUrl);
    thunkAPI.dispatch(getShopsAsAdminForPage({}));
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
      .addMatcher(isFulfilled(getShopsAsAdminForPage), (state, action) => {
        state.loading = false;
        state.shopsForPage = action.payload.data;
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
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
      .addMatcher(isPending(getShopsAsAdminForPage, getShopsAsAdmin, getShop), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createShop, updateShop, deleteShop, createShopModels), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      })
      .addMatcher(
        isRejected(getShopsAsAdminForPage, getShopsAsAdmin, getShop, createShop, updateShop, deleteShop, createShopModels),
        (state, action) => {
          state.loading = false;
          state.updating = false;
          state.updateSuccess = false;
          state.errorMessage = action.error.message;
        },
      );
  },
});

export const { reset } = ShopManagementSlice.actions;

// Reducer
export default ShopManagementSlice.reducer;
