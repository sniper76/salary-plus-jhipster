import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import ModelManagement from './model-management';
import ModelManagementDetail from './model-management-detail';
import ModelManagementUpdate from './model-management-update';

const ModelManagementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ModelManagement />} />
    <Route path=":shopId/new" element={<ModelManagementUpdate />} />
    <Route path=":userId/:shopId">
      <Route index element={<ModelManagementDetail />} />
      <Route path="edit" element={<ModelManagementUpdate />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ModelManagementRoutes;
