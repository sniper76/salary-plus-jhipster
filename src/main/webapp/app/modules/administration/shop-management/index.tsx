import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import ShopManagement from './shop-management';
import ShopManagementDetail from './shop-management-detail';
import ShopManagementUpdate from './shop-management-update';
import ShopManagementDeleteDialog from './shop-management-delete-dialog';

const ShopManagementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ShopManagement />} />
    <Route path="new" element={<ShopManagementUpdate />} />
    <Route path=":id">
      <Route index element={<ShopManagementDetail />} />
      <Route path="edit" element={<ShopManagementUpdate />} />
      <Route path="delete" element={<ShopManagementDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ShopManagementRoutes;
