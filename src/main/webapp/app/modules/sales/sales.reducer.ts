import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  salesList: [],
  sales: null,
  updating: false,
  updateSuccess: false,
  totalItems: 0,
};

const apiUrl = 'api/shops';

// Async Actions

export const getPageShopSales = createAsyncThunk('management/sales_page', async ({ shopId, startDate, endDate }: any) => {
  const requestUrl = `${apiUrl}/${shopId}/sales/${startDate}/${endDate}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<any[]>(requestUrl);
});

export const getShopSales = createAsyncThunk('management/sales_item', async ({ shopId, date }: any) => {
  const requestUrl = `${apiUrl}/${shopId}/sales/dates/${date}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<any>(requestUrl);
});

export type SalesState = Readonly<typeof initialState>;

export const SalesSlice = createSlice({
  name: 'sales',
  initialState: initialState as SalesState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getShopSales.fulfilled, (state, action) => {
        state.loading = false;
        state.sales = action.payload.data;
      })
      .addMatcher(isFulfilled(getPageShopSales), (state, action) => {
        state.loading = false;
        state.salesList = action.payload.data;
        console.warn('action.payload.data', action.payload.data);
      })
      .addMatcher(isPending(getPageShopSales, getShopSales), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isRejected(getPageShopSales, getShopSales), (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const { reset } = SalesSlice.actions;

// Reducer
export default SalesSlice.reducer;
