import axios from 'axios';
import { createAsyncThunk, createSlice, isPending, isRejected } from '@reduxjs/toolkit';

import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  users: [],
};

// Actions

export type WorkState = Readonly<typeof initialState>;

// Actions

export const getShopUsers = createAsyncThunk('work/shop_users', async ({ shopId, date }: any) => {
  const requestUrl = `api/shops/${shopId}/dates/${date}`;
  // console.warn('requestUrl', requestUrl);
  return axios.get<any[]>(requestUrl);
});

export const createCheckIn = createAsyncThunk(
  'work/create_checkIn',
  async ({ shopId, date, userId }: any, thunkAPI) => {
    const requestUrl = `api/shops/${shopId}/dates/${date}/users/${userId}`;
    return await axios.post(requestUrl);
    // thunkAPI.dispatch(getShopUsers({ shopId, date }));
  },
  { serializeError: serializeAxiosError },
);

export const createAllCheckIn = createAsyncThunk(
  'work/create_checkIn',
  async ({ shopId, date }: any, thunkAPI) => {
    const requestUrl = `api/shops/${shopId}/dates/${date}/users/all`;
    return await axios.post(requestUrl);
    // thunkAPI.dispatch(getShopUsers({ shopId, date }));
  },
  { serializeError: serializeAxiosError },
);

export const createAbsence = createAsyncThunk(
  'work/create_absence',
  async ({ shopId, date, userId }: any, thunkAPI) => {
    const requestUrl = `api/shops/${shopId}/dates/${date}/users/${userId}/absence`;
    return await axios.post(requestUrl);
    // thunkAPI.dispatch(getShopUsers({ shopId, date }));
  },
  { serializeError: serializeAxiosError },
);

export const updateHalfSalary = createAsyncThunk(
  'work/update_half_salary',
  async ({ shopId, date, userId }: any, thunkAPI) => {
    const requestUrl = `api/shops/${shopId}/dates/${date}/users/half-salaries/${userId}`;
    return await axios.post(requestUrl);
    // thunkAPI.dispatch(getShopUsers({ shopId, date }));
  },
  { serializeError: serializeAxiosError },
);

export const updateAllHalfSalary = createAsyncThunk(
  'work/update_all_half_salary',
  async ({ shopId, date }: any, thunkAPI) => {
    const requestUrl = `api/shops/${shopId}/dates/${date}/users/half-salaries/all`;
    return await axios.post(requestUrl);
    // thunkAPI.dispatch(getShopUsers({ shopId, date }));
  },
  { serializeError: serializeAxiosError },
);

export const WorkSlice = createSlice({
  name: 'works',
  initialState: initialState as WorkState,
  reducers: {},
  extraReducers(builder) {
    builder
      .addCase(getShopUsers.fulfilled, (state, action) => {
        state.loading = false;
        state.users = action.payload.data;
      })
      .addMatcher(isPending(getShopUsers), state => {
        state.errorMessage = null;
        state.loading = true;
      })
      .addMatcher(isPending(createCheckIn, createAllCheckIn, updateAllHalfSalary, updateHalfSalary), state => {
        state.errorMessage = null;
        state.loading = false;
      })
      .addMatcher(isRejected(getShopUsers, createCheckIn, createAllCheckIn, updateAllHalfSalary, updateHalfSalary), (state, action) => {
        state.errorMessage = action.error.message;
        state.loading = false;
      });
  },
});

// Reducer
export default WorkSlice.reducer;
