import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import SalesItemDiscount from './sales-item-discount';
import SalesItemDiscountUpdate from 'app/modules/management/sales-item-discount/sales-item-discount-update';
import SalesItemDiscountDetail from 'app/modules/management/sales-item-discount/sales-item-discount-detail';
import SalesItemDiscountDeleteDialog from 'app/modules/management/sales-item-discount/sales-item-discount-delete-dialog';

const SalesItemDiscountRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SalesItemDiscount />} />
    <Route path=":shopId/new" element={<SalesItemDiscountUpdate />} />
    <Route path=":shopId/:salesItemDiscountId">
      <Route index element={<SalesItemDiscountDetail />} />
      <Route path="edit" element={<SalesItemDiscountUpdate />} />
      <Route path="delete" element={<SalesItemDiscountDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SalesItemDiscountRoutes;
