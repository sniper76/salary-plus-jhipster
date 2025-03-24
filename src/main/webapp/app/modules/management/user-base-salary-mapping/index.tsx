import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import UserBaseSalaryMapping from './user-base-salary-mapping';
import UserBaseSalaryMappingAllCreateDialog from 'app/modules/management/user-base-salary-mapping/user-base-salary-mapping-all-create-dialog';
import UserBaseSalaryMappingCreateDialog from 'app/modules/management/user-base-salary-mapping/user-base-salary-mapping-create-dialog';

const UserBaseSalaryMappingRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserBaseSalaryMapping />} />
    <Route path=":shopId">
      <Route path="all_create" element={<UserBaseSalaryMappingAllCreateDialog />} />
    </Route>
    <Route path=":shopId/:userId">
      <Route path="create" element={<UserBaseSalaryMappingCreateDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserBaseSalaryMappingRoutes;
