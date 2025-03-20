import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import SalesItem from './sales-item';
import SalesItemUpdate from 'app/modules/management/sales-item/sales-item-update';

const SalesItemRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SalesItem />} />
    <Route path="new" element={<SalesItemUpdate />} />
    <Route path=":shopId/:salesItemId">
      <Route path="edit" element={<SalesItemUpdate />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SalesItemRoutes;
