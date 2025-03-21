import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import Penalty from './penalty';
import PenaltyUpdate from 'app/modules/management/penalty/penalty-update';
import PenaltyDetail from 'app/modules/management/penalty/penalty-detail';
import PenaltyDeleteDialog from 'app/modules/management/penalty/penalty-delete-dialog';

const PenaltyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Penalty />} />
    <Route path=":shopId/new" element={<PenaltyUpdate />} />
    <Route path=":shopId/:shopPenaltyId">
      <Route index element={<PenaltyDetail />} />
      <Route path="edit" element={<PenaltyUpdate />} />
      <Route path="delete" element={<PenaltyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PenaltyRoutes;
