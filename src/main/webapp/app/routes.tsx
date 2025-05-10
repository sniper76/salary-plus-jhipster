import React from 'react';
import { Route } from 'react-router';
import { Outlet } from 'react-router-dom';

import Loadable from 'react-loadable';

import Login from 'app/modules/login/login';
import Register from 'app/modules/account/register/register';
import Activate from 'app/modules/account/activate/activate';
import PasswordResetInit from 'app/modules/account/password-reset/init/password-reset-init';
import PasswordResetFinish from 'app/modules/account/password-reset/finish/password-reset-finish';
import Logout from 'app/modules/login/logout';
import Home from 'app/modules/home/home';
import Work from 'app/modules/work/work';
import Order from 'app/modules/order/order';
import Sales from 'app/modules/sales/sales';
import SalesDetail from 'app/modules/sales/sales-detail';
import Salary from 'app/modules/salary/salary';
import Statistics from 'app/modules/statistics/statistics';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PageNotFound from 'app/shared/error/page-not-found';
import { AUTHORITIES } from 'app/config/constants';
import WorkAttendanceDialog from 'app/modules/work/work-attendance-dialog';

const loading = <div>loading ...</div>;

const Account = Loadable({
  loader: () => import(/* webpackChunkName: "account" */ 'app/modules/account'),
  loading: () => loading,
});

const Admin = Loadable({
  loader: () => import(/* webpackChunkName: "administration" */ 'app/modules/administration'),
  loading: () => loading,
});

const Management = Loadable({
  loader: () => import(/* webpackChunkName: "management" */ 'app/modules/management'),
  loading: () => loading,
});
const AppRoutes = () => {
  return (
    <div className="view-routes">
      <ErrorBoundaryRoutes>
        <Route index element={<Home />} />
        <Route
          path="work"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.MANAGER, AUTHORITIES.USER]}>
              <Outlet /> {/* 중첩된 라우트의 렌더링 위치 */}
            </PrivateRoute>
          }
        >
          <Route index element={<Work />} />
          <Route path=":shopId/:userId/:date" element={<WorkAttendanceDialog />} />
        </Route>
        <Route
          path="order"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.MANAGER, AUTHORITIES.USER]}>
              <Order />
            </PrivateRoute>
          }
        />
        <Route
          path="sales"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.MANAGER, AUTHORITIES.CEO, AUTHORITIES.INVESTOR]}>
              <Outlet /> {/* 중첩된 라우트의 렌더링 위치 */}
            </PrivateRoute>
          }
        >
          <Route index element={<Sales />} />
          <Route path=":shopId/:date" element={<SalesDetail />} />
        </Route>
        <Route
          path="salary"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.MANAGER, AUTHORITIES.CEO, AUTHORITIES.INVESTOR]}>
              <Outlet /> {/* 중첩된 라우트의 렌더링 위치 */}
            </PrivateRoute>
          }
        >
          <Route index element={<Salary />} />
        </Route>
        <Route
          path="statistics"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.CEO, AUTHORITIES.INVESTOR]}>
              <Outlet /> {/* 중첩된 라우트의 렌더링 위치 */}
            </PrivateRoute>
          }
        >
          <Route index element={<Statistics />} />
        </Route>
        <Route path="login" element={<Login />} />
        <Route path="logout" element={<Logout />} />
        <Route path="account">
          <Route
            path="*"
            element={
              <PrivateRoute
                hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER, AUTHORITIES.MANAGER, AUTHORITIES.CEO, AUTHORITIES.INVESTOR]}
              >
                <Account />
              </PrivateRoute>
            }
          />
          <Route path="register" element={<Register />} />
          <Route path="activate" element={<Activate />} />
          <Route path="reset">
            <Route path="request" element={<PasswordResetInit />} />
            <Route path="finish" element={<PasswordResetFinish />} />
          </Route>
        </Route>
        <Route
          path="admin/*"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.ADMIN]}>
              <Admin />
            </PrivateRoute>
          }
        />
        <Route
          path="*"
          element={
            <PrivateRoute hasAnyAuthorities={[AUTHORITIES.MANAGER, AUTHORITIES.CEO]}>
              <Management />
            </PrivateRoute>
          }
        />
        <Route path="*" element={<PageNotFound />} />
      </ErrorBoundaryRoutes>
    </div>
  );
};

export default AppRoutes;
