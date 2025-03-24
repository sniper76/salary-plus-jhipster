import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IModelUser } from 'app/shared/model/modelUser.model';
import { IUserBaseSalaryMapping } from 'app/shared/model/userBaseSalaryMapping.model';

const initialState = {
  loading: false,
  errorMessage: null,
  users: [] as ReadonlyArray<IModelUser>,
  authorities: [] as any[],
  updating: false,
  updateSuccess: false,
  totalItems: 0,
};

const apiUrl = 'api/shops';

// Async Actions

export const getBaseSalaryMappingUsers = createAsyncThunk(
  'userBaseSalaryMapping/fetch_all_mapping_users',
  async ({ id, page, size, sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}/${id}/user-base-salary-mappings${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
    return axios.get<IModelUser[]>(requestUrl);
  },
);

export const createMappingAllUsers = createAsyncThunk(
  'userBaseSalaryMapping/create_all_mapping_users',
  async (user: IUserBaseSalaryMapping, thunkAPI) => {
    const requestUrl = `${apiUrl}/${user.shopId}/user-base-salary-mappings/all`;
    console.warn('createMappingAllUsers', requestUrl, user);
    const result = await axios.post<IUserBaseSalaryMapping>(requestUrl, user);
    thunkAPI.dispatch(getBaseSalaryMappingUsers({ id: user.shopId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const createMappingUsers = createAsyncThunk(
  'userBaseSalaryMapping/create_mapping_users',
  async (user: IUserBaseSalaryMapping, thunkAPI) => {
    const requestUrl = `${apiUrl}/${user.shopId}/user-base-salary-mappings/${user.userId}`;
    console.warn('createMappingUsers', requestUrl, user);
    const result = await axios.post<IUserBaseSalaryMapping>(requestUrl, user);
    thunkAPI.dispatch(getBaseSalaryMappingUsers({ id: user.shopId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export type UserSalaryMappingState = Readonly<typeof initialState>;

export const UserSalaryMappingSlice = createSlice({
  name: 'userBaseSalaryMappings',
  initialState: initialState as UserSalaryMappingState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(createMappingAllUsers.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
      })
      .addCase(createMappingUsers.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
      })
      .addMatcher(isFulfilled(getBaseSalaryMappingUsers), (state, action) => {
        state.loading = false;
        state.users = action.payload.data;
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
      })
      .addMatcher(isFulfilled(createMappingAllUsers, createMappingUsers), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
      })
      .addMatcher(isPending(getBaseSalaryMappingUsers), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createMappingAllUsers, createMappingAllUsers, createMappingUsers), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      })
      .addMatcher(isRejected(getBaseSalaryMappingUsers, createMappingAllUsers, createMappingUsers), (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const { reset } = UserSalaryMappingSlice.actions;

// Reducer
export default UserSalaryMappingSlice.reducer;
