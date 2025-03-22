import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import BaseSalary from './base-salary';
import BaseSalaryUpdate from 'app/modules/management/base-salary/base-salary-update';
import BaseSalaryDetail from 'app/modules/management/base-salary/base-salary-detail';
import BaseSalaryDeleteDialog from 'app/modules/management/base-salary/base-salary-delete-dialog';

const BaseSalaryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BaseSalary />} />
    <Route path=":shopId/new" element={<BaseSalaryUpdate />} />
    <Route path=":shopId/:shopBaseSalaryId">
      <Route index element={<BaseSalaryDetail />} />
      <Route path="edit" element={<BaseSalaryUpdate />} />
      <Route path="delete" element={<BaseSalaryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BaseSalaryRoutes;
