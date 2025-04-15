import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { ISalaryDetail } from 'app/shared/model/salaryDetail.model';

const initialState = {
  loading: false,
  errorMessage: null,
  salaryList: [],
  salaryForUser: [] as ReadonlyArray<ISalaryDetail>,
  salaryDetailList: [],
  updating: false,
  updateSuccess: false,
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

export const getShopSalaryForUser = createAsyncThunk('management/salary_user_list', async ({ id, userId, startDate, endDate }: any) => {
  const requestUrl = `${apiUrl}/${id}/daily-salaries/users/${userId}/${startDate}/${endDate}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<ISalaryDetail[]>(requestUrl);
});

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
      .addMatcher(isFulfilled(getShopSalaryForUser), (state, action) => {
        state.loading = false;
        state.salaryForUser = action.payload.data;
      })
      .addMatcher(isPending(getShopDailySalaryList, getShopSalaryForUser, getShopSalaryDetailList), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isRejected(getShopDailySalaryList, getShopSalaryForUser, getShopSalaryDetailList), (state, action) => {
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
