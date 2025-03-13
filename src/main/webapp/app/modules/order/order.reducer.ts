import axios from 'axios';
import { createAsyncThunk, createSlice, isPending, isRejected } from '@reduxjs/toolkit';

import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IShop } from 'app/shared/model/shop.model';
import { IShopSalesItem } from 'app/shared/model/shopSalesItem.model';
import { IModelUser } from 'app/shared/model/modelUser.model';
import { IShopTable } from 'app/shared/model/shopTable.model';
import { IOrderCreate } from 'app/shared/model/orderCreate.model';

const initialState = {
  loading: false,
  errorMessage: null,
  orders: [] as any,
  orderDetails: [] as any,
  salesItems: [] as ReadonlyArray<IShopSalesItem>,
  models: [] as ReadonlyArray<IModelUser>,
  tables: [] as ReadonlyArray<IShopTable>,
  shops: [] as ReadonlyArray<IShop>,
};

// Actions
export type OrderState = Readonly<typeof initialState>;

// Actions

export const getShopOrders = createAsyncThunk('order/shop_orders', async ({ shopId, date }: any) => {
  const requestUrl = `api/shops/${shopId}/orders/${date}`;
  return axios.get<any[]>(requestUrl);
});

export const getShopOrderDetails = createAsyncThunk('order/shop_order_details', async ({ shopId, date, orderId }: any) => {
  const requestUrl = `api/shops/${shopId}/orders/${date}/details/${orderId}`;
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

export const getShopTables = createAsyncThunk('order/shop_tables', async ({ shopId }: any) => {
  const requestUrl = `api/shops/${shopId}/tables`;
  return axios.get<any[]>(requestUrl);
});

export const createOrder = createAsyncThunk(
  'order/create_order',
  async (user: IOrderCreate, thunkAPI) => {
    const shopId = user.shopId;
    const date = user.date;
    const requestUrl = `api/shops/${shopId}/orders/${date}`;
    const result = await axios.post<IOrderCreate>(requestUrl, user);
    thunkAPI.dispatch(getShopOrders({ shopId, date }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateOrderPaid = createAsyncThunk(
  'order/update_paid_order',
  async ({ shopId, date, orderId }: any, thunkAPI) => {
    const requestUrl = `api/shops/${shopId}/orders/${date}/${orderId}`;
    const result = await axios.put<any>(requestUrl, { shopId, date, orderId });
    thunkAPI.dispatch(getShopOrders({ shopId, date }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

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
      .addCase(getShopOrderDetails.fulfilled, (state, action) => {
        state.loading = false;
        state.orderDetails = action.payload.data;
      })
      .addCase(getShopSalesItems.fulfilled, (state, action) => {
        state.loading = false;
        state.salesItems = action.payload.data;
      })
      .addCase(getShopModels.fulfilled, (state, action) => {
        state.loading = false;
        state.models = action.payload.data;
      })
      .addCase(getShopTables.fulfilled, (state, action) => {
        state.loading = false;
        state.tables = action.payload.data;
      })
      .addCase(getUserShops.fulfilled, (state, action) => {
        state.loading = false;
        state.shops = action.payload.data;
      })
      .addMatcher(isPending(getShopOrders, getShopOrderDetails, getUserShops, getShopSalesItems, getShopModels, getShopTables), state => {
        state.errorMessage = null;
        state.loading = true;
      })
      .addMatcher(isPending(createOrder, updateOrderPaid), state => {
        state.errorMessage = null;
        state.loading = false;
      })
      .addMatcher(
        isRejected(
          getShopOrders,
          getShopOrderDetails,
          getUserShops,
          getShopSalesItems,
          getShopModels,
          getShopTables,
          createOrder,
          updateOrderPaid,
        ),
        (state, action) => {
          state.errorMessage = action.error.message;
          state.loading = false;
        },
      );
  },
});

// Reducer
export default OrderSlice.reducer;
