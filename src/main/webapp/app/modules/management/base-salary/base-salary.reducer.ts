import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { defaultValue, IShopBaseSalary } from 'app/shared/model/baseSalary.model';

const initialState = {
  loading: false,
  errorMessage: null,
  baseSalary: defaultValue,
  baseSalariesForPage: [] as ReadonlyArray<IShopBaseSalary>,
  updating: false,
  updateSuccess: false,
  totalItems: 0,
};

const apiUrl = 'api/shops';

// Async Actions

export const getPageShopBaseSalaries = createAsyncThunk('management/baseSalaries_page', async ({ id, page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}/${id}/base-salaries${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<IShopBaseSalary[]>(requestUrl);
});

export const getShopBaseSalaries = createAsyncThunk('management/baseSalaries_selectBox', async ({ shopId }: any) => {
  const requestUrl = `${apiUrl}/${shopId}/base-salaries`;
  return axios.get<IShopBaseSalary[]>(requestUrl);
});

export const getShopBaseSalary = createAsyncThunk('management/baseSalary', async ({ shopId, shopBaseSalaryId }: any) => {
  const requestUrl = `${apiUrl}/${shopId}/base-salaries/${shopBaseSalaryId}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<IShopBaseSalary>(requestUrl);
});

export const createBaseSalary = createAsyncThunk(
  'management/create_baseSalary',
  async (user: IShopBaseSalary, thunkAPI) => {
    const requestUrl = `${apiUrl}/${user.shopId}/base-salaries`;
    console.warn('requestUrl', requestUrl);
    const result = await axios.post<IShopBaseSalary>(requestUrl, user);
    thunkAPI.dispatch(getPageShopBaseSalaries({ id: user.shopId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateBaseSalary = createAsyncThunk(
  'management/update_baseSalary',
  async (user: IShopBaseSalary, thunkAPI) => {
    const requestUrl = `${apiUrl}/${user.shopId}/base-salaries`;
    const result = await axios.put<IShopBaseSalary>(requestUrl, user);
    thunkAPI.dispatch(getPageShopBaseSalaries({ id: user.shopId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteBaseSalary = createAsyncThunk(
  'management/delete_baseSalary',
  async ({ shopId, shopBaseSalaryId }: any, thunkAPI) => {
    const requestUrl = `${apiUrl}/${shopId}/base-salaries/${shopBaseSalaryId}`;
    const result = await axios.delete<IShopBaseSalary>(requestUrl);
    thunkAPI.dispatch(getPageShopBaseSalaries({ id: shopId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export type BaseSalaryState = Readonly<typeof initialState>;

export const BaseSalarySlice = createSlice({
  name: 'baseSalaries',
  initialState: initialState as BaseSalaryState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getShopBaseSalary.fulfilled, (state, action) => {
        state.loading = false;
        state.baseSalary = action.payload.data;
      })
      .addCase(deleteBaseSalary.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.baseSalary = defaultValue;
      })
      .addMatcher(isFulfilled(getPageShopBaseSalaries, getShopBaseSalaries), (state, action) => {
        state.loading = false;
        state.baseSalariesForPage = action.payload.data;
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
      })
      .addMatcher(isFulfilled(createBaseSalary, updateBaseSalary), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.baseSalary = action.payload.data;
      })
      .addMatcher(isPending(getPageShopBaseSalaries, getShopBaseSalaries, getShopBaseSalary), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createBaseSalary, updateBaseSalary, deleteBaseSalary), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      })
      .addMatcher(isRejected(getPageShopBaseSalaries, getShopBaseSalaries, getShopBaseSalary), (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const { reset } = BaseSalarySlice.actions;

// Reducer
export default BaseSalarySlice.reducer;
