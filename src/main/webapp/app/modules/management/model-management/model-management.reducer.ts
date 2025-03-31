import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { defaultValue, IUser } from 'app/shared/model/user.model';
import { IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IShop } from 'app/shared/model/shop.model';

const initialState = {
  loading: false,
  errorMessage: null,
  users: [] as ReadonlyArray<IUser>,
  onlyModelUsers: [] as ReadonlyArray<IUser>,
  selectBoxShops: [] as ReadonlyArray<IShop>,
  authorities: [] as any[],
  user: defaultValue,
  updating: false,
  updateSuccess: false,
  totalItems: 0,
};

const apiUrl = 'api/shops';

// Async Actions

export const getUsers = createAsyncThunk('modelManagement/fetch_users', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return axios.get<IUser[]>(requestUrl);
});

export const getUsersAsAdmin = createAsyncThunk('modelManagement/fetch_users_as_admin', async ({ id, page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}/${id}/users${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return axios.get<IUser[]>(requestUrl);
});

export const getShopRoles = createAsyncThunk('modelManagement/fetch_shop_roles', async () => {
  const response = await axios.get<any[]>(`api/shops/authorities`);
  response.data = response?.data?.map(authority => authority.name);
  return response;
});

export const getUser = createAsyncThunk(
  'modelManagement/fetch_user',
  async ({ userId, shopId }: any) => {
    const requestUrl = `${apiUrl}/${shopId}/users/${userId}`;
    return axios.get<IUser>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getShopOnlyModels = createAsyncThunk('order/shop_only_models', async ({ shopId, userId }: any) => {
  const requestUrl = `api/shops/${shopId}/only-models/${userId}`;
  return axios.get<any[]>(requestUrl);
});

export const createUser = createAsyncThunk(
  'modelManagement/create_user',
  async (user: IUser, thunkAPI) => {
    const requestUrl = `${apiUrl}/${user.shopId}/users`;
    const result = await axios.post<IUser>(requestUrl, user);
    const params: IQueryParams = { id: parseInt(`${user.shopId}`, 10) };
    thunkAPI.dispatch(getUsersAsAdmin(params));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateUser = createAsyncThunk(
  'modelManagement/update_user',
  async (user: IUser, thunkAPI) => {
    const requestUrl = `${apiUrl}/${user.shopId}/users/${user.id}`;
    const result = await axios.put<IUser>(requestUrl, user);
    const params: IQueryParams = { id: parseInt(`${user.shopId}`, 10) };
    thunkAPI.dispatch(getUsersAsAdmin(params));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const updateUserMamaMappings = createAsyncThunk(
  'modelManagement/update_mama_mappings',
  async ({ shopId, userId, modelUserIds }: any, thunkAPI) => {
    const requestUrl = `${apiUrl}/${shopId}/models/${userId}/mama-mappings`;
    const result = axios.put(requestUrl, { modelUserIds });
    // thunkAPI.dispatch(getShopOnlyModels({ shopId, userId }));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export type ModelManagementState = Readonly<typeof initialState>;

export const ModelManagementSlice = createSlice({
  name: 'modelManagement',
  initialState: initialState as ModelManagementState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getShopRoles.fulfilled, (state, action) => {
        state.authorities = action.payload.data;
      })
      .addCase(getUser.fulfilled, (state, action) => {
        state.loading = false;
        state.user = action.payload.data;
      })
      .addCase(getShopOnlyModels.fulfilled, (state, action) => {
        state.loading = false;
        state.onlyModelUsers = action.payload.data;
      })
      .addMatcher(isFulfilled(getUsers, getUsersAsAdmin), (state, action) => {
        state.loading = false;
        state.users = action.payload.data;
        state.totalItems = parseInt(action.payload.headers['x-total-count'], 10);
      })
      .addMatcher(isFulfilled(createUser, updateUser), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.user = action.payload.data;
      })
      .addMatcher(isPending(getUsers, getUsersAsAdmin, getUser, getShopOnlyModels), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createUser, updateUser, updateUserMamaMappings), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      })
      .addMatcher(
        isRejected(getUsers, getUsersAsAdmin, getUser, getShopRoles, getShopOnlyModels, createUser, updateUser, updateUserMamaMappings),
        (state, action) => {
          state.loading = false;
          state.updating = false;
          state.updateSuccess = false;
          state.errorMessage = action.error.message;
        },
      );
  },
});

export const { reset } = ModelManagementSlice.actions;

// Reducer
export default ModelManagementSlice.reducer;
