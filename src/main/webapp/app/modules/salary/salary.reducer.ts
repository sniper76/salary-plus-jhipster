import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  salaryList: [],
  salaryForPage: [],
  salaryDetailList: [],
  updating: false,
  updateSuccess: false,
  totalItems: 0,
};

const apiUrl = 'api/shops';

export const getShopDailySalaryList = createAsyncThunk('management/salary_list', async ({ shopId, startDate, endDate }: any) => {
  const requestUrl = `${apiUrl}/${shopId}/daily-salaries/${startDate}/${endDate}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<any[]>(requestUrl);
});

export const getShopSalaryDetailList = createAsyncThunk('management/salary_detail_list', async ({ shopId, userId }: any) => {
  const requestUrl = `${apiUrl}/${shopId}/daily-salaries/users/${userId}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<any[]>(requestUrl);
});

export const getShopSalaryForPage = createAsyncThunk(
  'management/salary_list_page',
  async ({ id, query, page, size, sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}/${id}/daily-salaries/dates/${query}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
    console.warn('requestUrl', requestUrl);
    return axios.get<any[]>(requestUrl);
  },
);

export type SalaryState = Readonly<typeof initialState>;

export const SalarySlice = createSlice({
  name: 'salaries',
  initialState: initialState as SalaryState,
  reducers: {
    reset() {
      return initialState;
    },
    resetSalaryDetailList(state) {
      state.salaryDetailList = []; // 상세 데이터 초기화
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getShopDailySalaryList.fulfilled, (state, action) => {
        state.loading = false;
        state.salaryList = action.payload.data;
      })
      .addCase(getShopSalaryDetailList.fulfilled, (state, action) => {
        state.loading = false;
        state.salaryDetailList = action.payload.data;
      })
      .addMatcher(isFulfilled(getShopSalaryForPage), (state, action) => {
        state.loading = false;
        state.salaryForPage = action.payload.data;
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
      })
      .addMatcher(isPending(getShopDailySalaryList, getShopSalaryForPage, getShopSalaryDetailList), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isRejected(getShopDailySalaryList, getShopSalaryForPage, getShopSalaryDetailList), (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const { reset } = SalarySlice.actions;

export const { resetSalaryDetailList } = SalarySlice.actions;

export default SalarySlice.reducer;
