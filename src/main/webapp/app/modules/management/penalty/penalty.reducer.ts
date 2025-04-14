import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { defaultValue, IShopPenalty } from 'app/shared/model/penalty.model';

const initialState = {
  loading: false,
  errorMessage: null,
  penalty: defaultValue,
  penalties: [] as ReadonlyArray<IShopPenalty>,
  penaltiesForPage: [] as ReadonlyArray<IShopPenalty>,
  updating: false,
  updateSuccess: false,
  totalItems: 0,
};

const apiUrl = 'api/shops/';

// Async Actions

export const getPageShopPenalties = createAsyncThunk('management/penalties_page', async ({ id, page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}${id}/penalties${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<IShopPenalty[]>(requestUrl);
});

export const getShopPenalties = createAsyncThunk('management/penalties_list', async ({ id }: IQueryParams) => {
  const requestUrl = `${apiUrl}${id}/penalties/all`;
  console.warn('requestUrl', requestUrl);
  return axios.get<IShopPenalty[]>(requestUrl);
});

export const getShopPenalty = createAsyncThunk('management/penalty', async ({ shopId, shopPenaltyId }: any) => {
  const requestUrl = `${apiUrl}${shopId}/penalties/${shopPenaltyId}`;
  console.warn('requestUrl', requestUrl);
  return axios.get<IShopPenalty>(requestUrl);
});

export const createPenalty = createAsyncThunk(
  'management/create_penalty',
  async (user: IShopPenalty, thunkAPI) => {
    const requestUrl = `${apiUrl}${user.shopId}/penalties`;
    console.warn('requestUrl', requestUrl);
    const result = await axios.post<IShopPenalty>(requestUrl, user);
    thunkAPI.dispatch(getPageShopPenalties({ id: user.shopId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updatePenalty = createAsyncThunk(
  'management/update_penalty',
  async (user: IShopPenalty, thunkAPI) => {
    const requestUrl = `${apiUrl}${user.shopId}/penalties`;
    const result = await axios.put<IShopPenalty>(requestUrl, user);
    thunkAPI.dispatch(getPageShopPenalties({ id: user.shopId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deletePenalty = createAsyncThunk(
  'management/delete_penalty',
  async ({ shopId, shopPenaltyId }: any, thunkAPI) => {
    const requestUrl = `${apiUrl}${shopId}/penalties/${shopPenaltyId}`;
    const result = await axios.delete<IShopPenalty>(requestUrl);
    thunkAPI.dispatch(getPageShopPenalties({ id: shopId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export type PenaltyState = Readonly<typeof initialState>;

export const PenaltySlice = createSlice({
  name: 'penalties',
  initialState: initialState as PenaltyState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getShopPenalty.fulfilled, (state, action) => {
        state.loading = false;
        state.penalty = action.payload.data;
      })
      .addCase(deletePenalty.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.penalty = defaultValue;
      })
      .addMatcher(isFulfilled(getPageShopPenalties), (state, action) => {
        state.loading = false;
        state.penaltiesForPage = action.payload.data;
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
      })
      .addMatcher(isFulfilled(getShopPenalties), (state, action) => {
        state.loading = false;
        state.penalties = action.payload.data;
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
      })
      .addMatcher(isFulfilled(createPenalty, updatePenalty), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.penalty = action.payload.data;
      })
      .addMatcher(isPending(getPageShopPenalties, getShopPenalties, getShopPenalty), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createPenalty, updatePenalty, deletePenalty), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      })
      .addMatcher(isRejected(getPageShopPenalties, getShopPenalty, getShopPenalties), (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const { reset } = PenaltySlice.actions;

// Reducer
export default PenaltySlice.reducer;
