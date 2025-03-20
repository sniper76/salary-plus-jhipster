import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import SalesItem from './sales-item';
import SalesItemUpdate from 'app/modules/management/sales-item/sales-item-update';
import SalesItemDetail from 'app/modules/management/sales-item/sales-item-detail';
import SalesItemDeleteDialog from 'app/modules/management/sales-item/sales-item-delete-dialog';

const SalesItemRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SalesItem />} />
    <Route path=":shopId/new" element={<SalesItemUpdate />} />
    <Route path=":shopId/:salesItemId">
      <Route index element={<SalesItemDetail />} />
      <Route path="edit" element={<SalesItemUpdate />} />
      <Route path="delete" element={<SalesItemDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SalesItemRoutes;
