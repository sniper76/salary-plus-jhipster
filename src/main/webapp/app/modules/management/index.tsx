import React from 'react';

import { Route } from 'react-router';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import SalesItem from './sales-item';
import Penalty from './penalty';
import BaseSalary from './base-salary';
import UserSalaryMapping from './user-base-salary-mapping';
import SalesItemDiscount from './sales-item-discount';
import ModelManagement from './model-management';

const ManagementRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route path="shop/sales-item/*" element={<SalesItem />} />
      <Route path="shop/penalty/*" element={<Penalty />} />
      <Route path="shop/base-salary/*" element={<BaseSalary />} />
      <Route path="shop/user-base-salary-mapping/*" element={<UserSalaryMapping />} />
      <Route path="shop/sales-item-discount/*" element={<SalesItemDiscount />} />
      <Route path="shop/model-management/*" element={<ModelManagement />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default ManagementRoutes;
