import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import UserSalaryMapping from './user-salary-mapping';
import UserSalaryMappingAllCreateDialog from 'app/modules/management/user-salary-mapping/user-salary-mapping-all-create-dialog';

const UserSalaryMappingRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserSalaryMapping />} />
    <Route path=":shopId">
      <Route path="all_create" element={<UserSalaryMappingAllCreateDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserSalaryMappingRoutes;
