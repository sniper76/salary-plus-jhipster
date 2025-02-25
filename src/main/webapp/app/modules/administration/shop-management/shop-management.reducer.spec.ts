import { configureStore } from '@reduxjs/toolkit';
import axios from 'axios';
import sinon from 'sinon';

import { defaultValue } from 'app/shared/model/shop.model';
import { AUTHORITIES } from 'app/config/constants';
import shopManagement, { createShop, deleteShop, getShop, getShopsAsAdmin, reset, updateShop } from './shop-management.reducer';

describe('User management reducer tests', () => {
  const id = process.env.E2E_USERNAME ?? 1;

  function isEmpty(element): boolean {
    if (element instanceof Array) {
      return element.length === 0;
    }
    return Object.keys(element).length === 0;
  }

  function testInitialState(state) {
    expect(state).toMatchObject({
      loading: false,
      errorMessage: null,
      updating: false,
      updateSuccess: false,
      totalItems: 0,
    });
    expect(isEmpty(state.shops));
    expect(isEmpty(state.authorities));
    expect(isEmpty(state.shop));
  }

  function testMultipleTypes(types, payload, testFunction, error?) {
    types.forEach(e => {
      testFunction(shopManagement(undefined, { type: e, payload, error }));
    });
  }

  describe('Common', () => {
    it('should return the initial state', () => {
      testInitialState(shopManagement(undefined, { type: 'unknown' }));
    });
  });

  describe('Requests', () => {
    it('should set state to loading', () => {
      testMultipleTypes([getShopsAsAdmin.pending.type, getShop.pending.type], {}, state => {
        expect(state).toMatchObject({
          errorMessage: null,
          updateSuccess: false,
          loading: true,
        });
      });
    });

    it('should set state to updating', () => {
      testMultipleTypes([createShop.pending.type, updateShop.pending.type, deleteShop.pending.type], {}, state => {
        expect(state).toMatchObject({
          errorMessage: null,
          updateSuccess: false,
          updating: true,
        });
      });
    });
  });

  describe('Failures', () => {
    it('should set state to failed and put an error message in errorMessage', () => {
      testMultipleTypes(
        [
          getShopsAsAdmin.rejected.type,
          getShop.rejected.type,
          createShop.rejected.type,
          updateShop.rejected.type,
          deleteShop.rejected.type,
        ],
        { message: 'something happened' },
        state => {
          expect(state).toMatchObject({
            loading: false,
            updating: false,
            updateSuccess: false,
            errorMessage: 'error happened',
          });
        },
        { message: 'error happened' },
      );
    });
  });

  describe('Success', () => {
    it('should update state according to a successful fetch user request', () => {
      const payload = { data: 'some handsome user' };
      const toTest = shopManagement(undefined, { type: getShop.fulfilled.type, payload });

      expect(toTest).toMatchObject({
        loading: false,
        user: payload.data,
      });
    });

    it('should set state to successful update', () => {
      testMultipleTypes([createShop.fulfilled.type, updateShop.fulfilled.type], { data: 'some handsome user' }, types => {
        expect(types).toMatchObject({
          updating: false,
          updateSuccess: true,
          user: 'some handsome user',
        });
      });
    });

    it('should set state to successful update with an empty user', () => {
      const toTest = shopManagement(undefined, { type: deleteShop.fulfilled.type });

      expect(toTest).toMatchObject({
        updating: false,
        updateSuccess: true,
      });
      expect(isEmpty(toTest.shop));
    });
  });

  describe('Reset', () => {
    it('should reset the state', () => {
      const initialState = {
        loading: false,
        errorMessage: null,
        shops: [],
        authorities: [] as any[],
        shop: defaultValue,
        updating: false,
        updateSuccess: false,
        totalItems: 0,
      };
      const initialStateNew = {
        ...initialState,
        loading: true,
      };
      expect(shopManagement(initialStateNew, reset)).toEqual(initialState);
    });
  });

  describe('Actions', () => {
    let store;

    const resolvedObject = { value: 'whatever' };
    const getState = jest.fn();
    const dispatch = jest.fn();
    const extra = {};
    beforeEach(() => {
      store = configureStore({
        reducer: (state = [], action) => [...state, action],
      });
      axios.get = sinon.stub().returns(Promise.resolve(resolvedObject));
      axios.put = sinon.stub().returns(Promise.resolve(resolvedObject));
      axios.post = sinon.stub().returns(Promise.resolve(resolvedObject));
      axios.delete = sinon.stub().returns(Promise.resolve(resolvedObject));
    });

    it('dispatches FETCH_USERS_AS_ADMIN_PENDING and FETCH_USERS_AS_ADMIN_FULFILLED actions', async () => {
      const arg = {};

      const result = await getShopsAsAdmin(arg)(dispatch, getState, extra);

      const pendingAction = dispatch.mock.calls[0][0];
      expect(pendingAction.meta.requestStatus).toBe('pending');
      expect(getShopsAsAdmin.fulfilled.match(result)).toBe(true);
    });

    it('dispatches FETCH_USERS_AS_ADMIN_PENDING and FETCH_USERS_AS_ADMIN_FULFILLED actions with pagination options', async () => {
      const arg = { page: 1, size: 20, sort: 'id,desc' };

      const result = await getShopsAsAdmin(arg)(dispatch, getState, extra);

      const pendingAction = dispatch.mock.calls[0][0];
      expect(pendingAction.meta.requestStatus).toBe('pending');
      expect(getShopsAsAdmin.fulfilled.match(result)).toBe(true);
    });

    it('dispatches FETCH_USER_PENDING and FETCH_USER_FULFILLED actions', async () => {
      const result = await getShop(id)(dispatch, getState, extra);

      const pendingAction = dispatch.mock.calls[0][0];
      expect(pendingAction.meta.requestStatus).toBe('pending');
      expect(getShop.fulfilled.match(result)).toBe(true);
    });

    it('dispatches CREATE_USER_PENDING and CREATE_USER_FULFILLED actions', async () => {
      const arg = {};

      const result = await createShop(arg)(dispatch, getState, extra);

      const pendingAction = dispatch.mock.calls[0][0];
      expect(pendingAction.meta.requestStatus).toBe('pending');
      expect(createShop.fulfilled.match(result)).toBe(true);
    });

    it('dispatches UPDATE_USER_PENDING and UPDATE_USER_FULFILLED actions', async () => {
      const arg = { id: id };

      const result = await updateShop(arg)(dispatch, getState, extra);

      const pendingAction = dispatch.mock.calls[0][0];
      expect(pendingAction.meta.requestStatus).toBe('pending');
      expect(updateShop.fulfilled.match(result)).toBe(true);
    });

    it('dispatches DELETE_USER_PENDING and DELETE_USER_FULFILLED actions', async () => {
      const result = await deleteShop(id)(dispatch, getState, extra);

      const pendingAction = dispatch.mock.calls[0][0];
      expect(pendingAction.meta.requestStatus).toBe('pending');
      expect(deleteShop.fulfilled.match(result)).toBe(true);
    });

    it('dispatches RESET actions', async () => {
      await store.dispatch(reset());
      expect(store.getState()).toEqual([expect.any(Object), expect.objectContaining(reset())]);
    });
  });
});
