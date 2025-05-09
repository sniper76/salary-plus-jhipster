import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';
import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  salesList: [],
  salesForPage: [],
  salesDetailList: [],
  salesPenaltyList: [],
  updating: false,
  updateSuccess: false,
  totalItems: 0,
};

const apiUrl = 'api/shops';

export const getShopSaleList = createAsyncThunk('management/sales_list', async ({ shopId, startDate, endDate }: any) => {
  const requestUrl = `${apiUrl}/${shopId}/sales/${startDate}/${endDate}`;
  return axios.get<any[]>(requestUrl);
});

export const getShopSaleDetailList = createAsyncThunk('management/sales_detail_list', async ({ shopId, orderId }: any) => {
  const requestUrl = `${apiUrl}/${shopId}/sales/orders/${orderId}`;
  return axios.get<any[]>(requestUrl);
});

export const getShopSalePenaltyList = createAsyncThunk('management/sales_penalty_list', async ({ shopId, date, orderId }: any) => {
  const requestUrl = `${apiUrl}/${shopId}/dates/${date}/orders/${orderId}/penalties`;
  return axios.get<any[]>(requestUrl);
});

export const getShopSalesForPage = createAsyncThunk('management/sales_list_page', async ({ id, query, page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}/${id}/sales/dates/${query}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return axios.get<any[]>(requestUrl);
});

export const SalesSlice = createSlice({
  name: 'sales',
  initialState,
  reducers: {
    reset(state) {
      return initialState;
    },
    resetSalesDetailList(state) {
      state.salesDetailList = []; // 주문 상세 리스트 초기화
      state.salesPenaltyList = [];
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
      .addCase(getShopSalePenaltyList.fulfilled, (state, action) => {
        state.loading = false;
        state.salesPenaltyList = action.payload.data;
      })
      .addCase(resetSalesDetailList, state => {
        state.salesDetailList = [];
      })
      .addMatcher(isFulfilled(getShopSalesForPage), (state, action) => {
        state.loading = false;
        state.salesForPage = action.payload.data;
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
      })
      .addMatcher(isPending(getShopSaleList, getShopSalesForPage, getShopSaleDetailList, getShopSalePenaltyList), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isRejected(getShopSaleList, getShopSalesForPage, getShopSaleDetailList, getShopSalePenaltyList), (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const { reset, resetSalesDetailList } = SalesSlice.actions;
export default SalesSlice.reducer;
