import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { defaultValue, IShopBaseSalary } from 'app/shared/model/baseSalary.model';

const initialState = {
  loading: false,
  errorMessage: null,
  bonusSalary: defaultValue,
  bonusSalariesForPage: [] as ReadonlyArray<IShopBaseSalary>,
  updating: false,
  updateSuccess: false,
  totalItems: 0,
};

const apiUrl = 'api/shops';

// Async Actions

export const getPageShopBonusSalaries = createAsyncThunk(
  'management/bonusSalaries_page',
  async ({ id, page, size, sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}/${id}/bonus-salaries${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
    console.warn('requestUrl', requestUrl);
    return axios.get<IShopBaseSalary[]>(requestUrl);
  },
);

export const getShopBonusSalaries = createAsyncThunk('management/bonusSalaries_selectBox', async ({ shopId }: any) => {
  const requestUrl = `${apiUrl}/${shopId}/bonus-salaries`;
  return axios.get<IShopBaseSalary[]>(requestUrl);
});

export const getShopBonusSalary = createAsyncThunk('management/bonusSalary', async ({ shopId, shopBonusSalaryId }: any) => {
  const requestUrl = `${apiUrl}/${shopId}/bonus-salaries/${shopBonusSalaryId}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<IShopBaseSalary>(requestUrl);
});

export const createBonusSalary = createAsyncThunk(
  'management/create_bonusSalary',
  async (user: IShopBaseSalary, thunkAPI) => {
    const requestUrl = `${apiUrl}/${user.shopId}/bonus-salaries`;
    console.warn('requestUrl', requestUrl);
    const result = await axios.post<IShopBaseSalary>(requestUrl, user);
    thunkAPI.dispatch(getPageShopBonusSalaries({ id: user.shopId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateBonusSalary = createAsyncThunk(
  'management/update_bonusSalary',
  async (user: IShopBaseSalary, thunkAPI) => {
    const requestUrl = `${apiUrl}/${user.shopId}/bonus-salaries`;
    const result = await axios.put<IShopBaseSalary>(requestUrl, user);
    thunkAPI.dispatch(getPageShopBonusSalaries({ id: user.shopId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteBonusSalary = createAsyncThunk(
  'management/delete_bonusSalary',
  async ({ shopId, shopBonusSalaryId }: any, thunkAPI) => {
    const requestUrl = `${apiUrl}/${shopId}/bonus-salaries/${shopBonusSalaryId}`;
    const result = await axios.delete<IShopBaseSalary>(requestUrl);
    thunkAPI.dispatch(getPageShopBonusSalaries({ id: shopId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export type BonusSalaryState = Readonly<typeof initialState>;

export const BonusSalarySlice = createSlice({
  name: 'bonusSalaries',
  initialState: initialState as BonusSalaryState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getShopBonusSalary.fulfilled, (state, action) => {
        state.loading = false;
        state.bonusSalary = action.payload.data;
      })
      .addCase(deleteBonusSalary.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.bonusSalary = defaultValue;
      })
      .addMatcher(isFulfilled(getPageShopBonusSalaries, getShopBonusSalaries), (state, action) => {
        state.loading = false;
        state.bonusSalariesForPage = action.payload.data;
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
      })
      .addMatcher(isFulfilled(createBonusSalary, updateBonusSalary), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.bonusSalary = action.payload.data;
      })
      .addMatcher(isPending(getPageShopBonusSalaries, getShopBonusSalaries, getShopBonusSalary), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createBonusSalary, updateBonusSalary, deleteBonusSalary), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      })
      .addMatcher(isRejected(getPageShopBonusSalaries, getShopBonusSalaries, getShopBonusSalary), (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const { reset } = BonusSalarySlice.actions;

// Reducer
export default BonusSalarySlice.reducer;
