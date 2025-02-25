import axios from 'axios';
import { createAsyncThunk, createSlice, isPending, isRejected } from '@reduxjs/toolkit';

import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IOrder } from 'app/shared/model/order.model';

const initialState = {
  loading: false,
  errorMessage: null,
  order: {
    configProps: {} as any,
    env: {} as any,
  },
};

// Actions
const apiUrl = 'api/account';

export type OrderState = Readonly<typeof initialState>;

// Actions

export const getUserRoles = createAsyncThunk('work/user_roles', async () => axios.get<any>('api/account/roles'), {
  serializeError: serializeAxiosError,
});

export const getOrder = createAsyncThunk(
  'order/fetch_order',
  async (id: number) => {
    const requestUrl = `${apiUrl}/order/${id}`;
    return axios.get<IOrder>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const OrderSlice = createSlice({
  name: 'orders',
  initialState: initialState as OrderState,
  reducers: {},
  extraReducers(builder) {
    builder
      .addCase(getUserRoles.fulfilled, (state, action) => {
        state.loading = false;
        state.order = {
          ...state.order,
          configProps: action.payload.data,
        };
      })
      .addMatcher(isPending(getUserRoles), state => {
        state.errorMessage = null;
        state.loading = true;
      })
      .addMatcher(isRejected(getUserRoles), (state, action) => {
        state.errorMessage = action.error.message;
        state.loading = false;
      });
  },
});

// Reducer
export default OrderSlice.reducer;
