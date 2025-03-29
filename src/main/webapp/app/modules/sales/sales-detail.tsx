import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Badge, Button, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_TIMESTAMP_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getShopSales } from './sales.reducer';

export const SalesDetail = () => {
  const dispatch = useAppDispatch();

  const { shopId, date } = useParams();

  useEffect(() => {
    dispatch(getShopSales({ shopId, date }));
  }, []);

  const sales = useAppSelector(state => state.sales.sales);

  return (
    <div>
      <h2>
        <Translate contentKey="sales.detail.title">상품</Translate>
      </h2>
      <Row size="md">
        <dl className="jh-entity-details">
          <dt>
            <Translate contentKey="global.field.id">ID</Translate>
          </dt>
        </dl>
      </Row>
      <Button tag={Link} to="/sales" replace color="info">
        <FontAwesomeIcon icon="arrow-left" />{' '}
        <span className="d-none d-md-inline">
          <Translate contentKey="entity.action.back">Back</Translate>
        </span>
      </Button>
    </div>
  );
};

export default SalesDetail;
