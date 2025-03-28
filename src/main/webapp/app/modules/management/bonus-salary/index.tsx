import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import BonusSalary from './bonus-salary';
import BonusSalaryUpdate from 'app/modules/management/bonus-salary/bonus-salary-update';
import BonusSalaryDetail from 'app/modules/management/bonus-salary/bonus-salary-detail';

const BonusSalaryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<BonusSalary />} />
    <Route path=":shopId/new" element={<BonusSalaryUpdate />} />
    <Route path=":shopId/:shopBonusSalaryId">
      <Route index element={<BonusSalaryDetail />} />
      <Route path="edit" element={<BonusSalaryUpdate />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BonusSalaryRoutes;
