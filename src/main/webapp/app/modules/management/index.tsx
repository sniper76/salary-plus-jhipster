import React from 'react';

import { Route } from 'react-router';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import SalesItem from './sales-item';
import Penalty from './penalty';
import BaseSalary from './base-salary';

const ManagementRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route path="shop/sales-item/*" element={<SalesItem />} />
      <Route path="shop/penalty/*" element={<Penalty />} />
      <Route path="shop/base-salary/*" element={<BaseSalary />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default ManagementRoutes;
