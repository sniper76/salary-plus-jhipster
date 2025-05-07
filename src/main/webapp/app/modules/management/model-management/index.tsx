import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import ModelManagement from './model-management';
import ModelManagementDetail from './model-management-detail';
import ModelManagementUpdate from './model-management-update';
import ModelManagementMamaMapping from './model-management-mama-mapping';
import ModelManagementDeleteDialog from './model-management-delete-dialog';

const ModelManagementRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ModelManagement />} />
    <Route path=":shopId/new" element={<ModelManagementUpdate />} />
    <Route path=":shopId/:userId">
      <Route index element={<ModelManagementDetail />} />
      <Route path="edit" element={<ModelManagementUpdate />} />
      <Route path="delete" element={<ModelManagementDeleteDialog />} />
      <Route path="mapping" element={<ModelManagementMamaMapping />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ModelManagementRoutes;
