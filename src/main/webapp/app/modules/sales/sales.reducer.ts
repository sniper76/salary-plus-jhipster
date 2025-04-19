import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  salesList: [],
  salesForPage: [],
  salesDetailList: [],
  updating: false,
  updateSuccess: false,
  totalItems: 0,
};

const apiUrl = 'api/shops';

// Async Actions

export const getShopSaleList = createAsyncThunk('management/sales_list', async ({ shopId, startDate, endDate }: any) => {
  const requestUrl = `${apiUrl}/${shopId}/sales/${startDate}/${endDate}`;
  // console.warn('requestUrl', requestUrl);
  return axios.get<any[]>(requestUrl);
});

export const getShopSaleDetailList = createAsyncThunk('management/sales_detail_list', async ({ shopId, orderId }: any) => {
  const requestUrl = `${apiUrl}/${shopId}/sales/orders/${orderId}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<any[]>(requestUrl);
});

export const getShopSalesForPage = createAsyncThunk('management/sales_list_page', async ({ id, query, page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}/${id}/sales/dates/${query}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<any[]>(requestUrl);
});

export type SalesState = Readonly<typeof initialState>;

export const SalesSlice = createSlice({
  name: 'sales',
  initialState: initialState as SalesState,
  reducers: {
    reset() {
      return initialState;
    },
    resetSalesDetailList(state) {
      state.salesDetailList = []; // 상세 데이터 초기화
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getShopSaleList.fulfilled, (state, action) => {
        state.loading = false;
        state.salesList = action.payload.data;
      })
      .addCase(getShopSaleDetailList.fulfilled, (state, action) => {
        state.loading = false;
        state.salesDetailList = action.payload.data;
      })
      .addMatcher(isFulfilled(getShopSalesForPage), (state, action) => {
        state.loading = false;
        state.salesForPage = action.payload.data;
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
      })
      .addMatcher(isPending(getShopSaleList, getShopSalesForPage, getShopSaleDetailList), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isRejected(getShopSaleList, getShopSalesForPage, getShopSaleDetailList), (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const { reset } = SalesSlice.actions;

export const { resetSalesDetailList } = SalesSlice.actions;

// Reducer
export default SalesSlice.reducer;
