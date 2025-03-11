import axios from 'axios';
import { createAsyncThunk, createSlice, isPending, isRejected } from '@reduxjs/toolkit';

import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IShop } from 'app/shared/model/shop.model';
import { IShopSalesItem } from 'app/shared/model/shopSalesItem.model';

const initialState = {
  loading: false,
  errorMessage: null,
  orders: [] as any,
  salesItems: [] as ReadonlyArray<IShopSalesItem>,
  models: [] as any,
  shops: [] as ReadonlyArray<IShop>,
};

// Actions
export type OrderState = Readonly<typeof initialState>;

// Actions

export const getShopOrders = createAsyncThunk('order/shop_orders', async ({ shopId, date }: any) => {
  const requestUrl = `api/shops/${shopId}/orders/${date}`;
  return axios.get<any[]>(requestUrl);
});

export const getUserShops = createAsyncThunk('order/user_shops', async () => axios.get<any>('api/shops'), {
  serializeError: serializeAxiosError,
});

export const getShopSalesItems = createAsyncThunk('order/shop_sales_items', async ({ shopId }: any) => {
  const requestUrl = `api/shops/${shopId}/sales-items`;
  return axios.get<any[]>(requestUrl);
});

export const getShopModels = createAsyncThunk('order/shop_models', async ({ shopId }: any) => {
  const requestUrl = `api/shops/${shopId}/models`;
  return axios.get<any[]>(requestUrl);
});

export const OrderSlice = createSlice({
  name: 'orders',
  initialState: initialState as OrderState,
  reducers: {},
  extraReducers(builder) {
    builder
      .addCase(getShopOrders.fulfilled, (state, action) => {
        state.loading = false;
        state.orders = action.payload.data;
      })
      .addCase(getShopSalesItems.fulfilled, (state, action) => {
        state.loading = false;
        state.salesItems = action.payload.data;
      })
      .addCase(getShopModels.fulfilled, (state, action) => {
        state.loading = false;
        state.models = action.payload.data;
      })
      .addCase(getUserShops.fulfilled, (state, action) => {
        state.shops = action.payload.data;
      })
      .addMatcher(isPending(getShopOrders, getUserShops, getShopSalesItems, getShopModels), state => {
        state.errorMessage = null;
        state.loading = true;
      })
      .addMatcher(isRejected(getShopOrders, getUserShops, getShopSalesItems, getShopModels), (state, action) => {
        state.errorMessage = action.error.message;
        state.loading = false;
      });
  },
});

// Reducer
export default OrderSlice.reducer;
