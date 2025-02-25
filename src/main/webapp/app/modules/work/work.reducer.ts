import axios from 'axios';
import { createAsyncThunk, createSlice, isPending, isRejected } from '@reduxjs/toolkit';

import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  work: {
    configProps: {} as any,
    env: {} as any,
  },
};

// Actions
// const apiUrl = 'api/account';

export type WorkState = Readonly<typeof initialState>;

// Actions

export const getUserRoles = createAsyncThunk('work/user_roles', async () => axios.get<any>('api/account/roles'), {
  serializeError: serializeAxiosError,
});

export const WorkSlice = createSlice({
  name: 'works',
  initialState: initialState as WorkState,
  reducers: {},
  extraReducers(builder) {
    builder
      .addCase(getUserRoles.fulfilled, (state, action) => {
        state.loading = false;
        state.work = {
          ...state.work,
          configProps: action.payload.data,
        };
      })
      .addMatcher(isPending(getUserRoles), state => {
        state.errorMessage = null;
        state.loading = true;
      })
      .addMatcher(isRejected(getUserRoles), (state, action) => {
        state.errorMessage = action.error.message;
        state.loading = false;
      });
  },
});

// Reducer
export default WorkSlice.reducer;
