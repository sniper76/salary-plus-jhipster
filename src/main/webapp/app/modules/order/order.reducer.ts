import axios from 'axios';
import { createAsyncThunk, createSlice, isPending, isRejected } from '@reduxjs/toolkit';

import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IShop } from 'app/shared/model/shop.model';
import { IShopSalesItem } from 'app/shared/model/shopSalesItem.model';
import { IModelUser } from 'app/shared/model/modelUser.model';
import { IShopTable } from 'app/shared/model/shopTable.model';
import { IOrderCreate } from 'app/shared/model/orderCreate.model';
import { IOrderDetail } from 'app/shared/model/orderDetail.model';
import { IOrderRefund } from 'app/shared/model/orderRefund.model';

const initialState = {
  loading: false,
  errorMessage: null,
  orders: [] as any,
  orderDetails: [] as ReadonlyArray<IOrderDetail>,
  orderDetailWithDiscounts: [] as any,
  salesItems: [] as ReadonlyArray<IShopSalesItem>,
  models: [] as ReadonlyArray<IModelUser>,
  tables: [] as ReadonlyArray<IShopTable>,
  shops: [] as ReadonlyArray<IShop>,
};

// Actions
export type OrderState = Readonly<typeof initialState>;

// Actions

export const getShopOrders = createAsyncThunk('order/shop_orders', async ({ shopId, date }: any) => {
  const requestUrl = `api/shops/${shopId}/dates/${date}/orders`;
  // console.warn('requestUrl', requestUrl);
  return axios.get<any[]>(requestUrl);
});

export const getShopOrderDetails = createAsyncThunk('order/shop_order_details', async ({ shopId, date, orderId }: any) => {
  const requestUrl = `api/shops/${shopId}/dates/${date}/orders/${orderId}`;
  // console.warn('requestUrl', requestUrl);
  return axios.get<IOrderDetail[]>(requestUrl);
});

export const getShopOrderDetailWithDiscounts = createAsyncThunk(
  'order/shop_order_detail_with_discounts',
  async ({ shopId, date, orderId }: any) => {
    const requestUrl = `api/shops/${shopId}/dates/${date}/orders/${orderId}/discounts`;
    // console.warn('requestUrl', requestUrl);
    return axios.get<IOrderDetail[]>(requestUrl);
  },
);

export const getUserShops = createAsyncThunk('order/user_shops', async () => axios.get<any>('api/shops'), {
  serializeError: serializeAxiosError,
});

export const getShopSalesItems = createAsyncThunk('order/shop_sales_items', async ({ shopId }: any) => {
  const requestUrl = `api/shops/${shopId}/sales-items/all`;
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
    const requestUrl = `api/shops/${shopId}/dates/${date}`;
    const result = await axios.post<IOrderCreate>(requestUrl, user);
    thunkAPI.dispatch(getShopOrders({ shopId, date }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const createOrderWithDiscounts = createAsyncThunk(
  'order/create_order',
  async (user: IOrderCreate, thunkAPI) => {
    const shopId = user.shopId;
    const date = user.date;
    const requestUrl = `api/shops/${shopId}/dates/${date}/discounts`;
    const result = await axios.post<IOrderCreate>(requestUrl, user);
    thunkAPI.dispatch(getShopOrders({ shopId, date }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateOrder = createAsyncThunk(
  'order/update_order',
  async (user: IOrderCreate, thunkAPI) => {
    const shopId = user.shopId;
    const date = user.date;
    const requestUrl = `api/shops/${shopId}/dates/${date}`;
    const result = await axios.patch<IOrderCreate>(requestUrl, user);
    thunkAPI.dispatch(getShopOrders({ shopId, date }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateOrderPaid = createAsyncThunk(
  'order/update_paid_order',
  async ({ shopId, date, orderId }: any, thunkAPI) => {
    const requestUrl = `api/shops/${shopId}/dates/${date}/orders/${orderId}`;
    const result = await axios.put<any>(requestUrl, { shopId, date, orderId });
    thunkAPI.dispatch(getShopOrders({ shopId, date }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateOrderRefund = createAsyncThunk(
  'order/update_refund_order',
  async (user: IOrderRefund, thunkAPI) => {
    const shopId = user.shopId;
    const date = user.date;
    const orderId = user.orderId;
    const requestUrl = `api/shops/${shopId}/dates/${date}/orders/${orderId}/refunds`;
    const result = await axios.post<IOrderRefund>(requestUrl, user);
    thunkAPI.dispatch(getShopOrders({ shopId, date }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteOrderDetail = createAsyncThunk(
  'order/delete_order_detail',
  async ({ orderId, orderDetailId }: any, thunkAPI) => {
    const requestUrl = `api/shops/orders/${orderId}/details/${orderDetailId}`;
    console.warn('requestUrl', requestUrl);
    await axios.delete<any>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const OrderSlice = createSlice({
  name: 'orders',
  initialState: initialState as OrderState,
  reducers: {
    clearOrderDetails(state) {
      state.orderDetails = []; // orderDetails 배열을 초기화
    },
  },
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
      .addCase(getShopOrderDetailWithDiscounts.fulfilled, (state, action) => {
        state.loading = false;
        state.orderDetailWithDiscounts = action.payload.data;
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
      .addMatcher(
        isPending(
          getShopOrders,
          getShopOrderDetails,
          getShopOrderDetailWithDiscounts,
          getUserShops,
          getShopSalesItems,
          getShopModels,
          getShopTables,
        ),
        state => {
          state.errorMessage = null;
          state.loading = true;
        },
      )
      .addMatcher(isPending(createOrder, updateOrderPaid, deleteOrderDetail), state => {
        state.errorMessage = null;
        state.loading = false;
      })
      .addMatcher(
        isRejected(
          getShopOrders,
          getShopOrderDetails,
          getShopOrderDetailWithDiscounts,
          getUserShops,
          getShopSalesItems,
          getShopModels,
          getShopTables,
          createOrder,
          updateOrderPaid,
          deleteOrderDetail,
        ),
        (state, action) => {
          state.errorMessage = action.error.message;
          state.loading = false;
        },
      );
  },
});

export const { clearOrderDetails } = OrderSlice.actions; // 초기화 액션 export

// Reducer
export default OrderSlice.reducer;
