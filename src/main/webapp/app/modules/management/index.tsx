import React from 'react';

import { Route } from 'react-router';
import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import SalesItem from './sales-item';

const ManagementRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route path="shop/sales-item/*" element={<SalesItem />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default ManagementRoutes;
