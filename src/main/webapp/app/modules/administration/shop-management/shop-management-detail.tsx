import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Badge, Button, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { languages } from 'app/config/translation';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getShop } from './shop-management.reducer';

export const ShopManagementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getShop(id));
  }, []);

  const shop = useAppSelector(state => state.shopManagement.shop);

  const types: any = [
    { key: 'BAR', name: '바' },
    { key: 'RESTAURANT', name: '식당' },
  ];

  return (
    <div>
      <h2>
        <Translate contentKey="shopManagement.detail.title">Shop</Translate> [<strong>{shop.id}</strong>]
      </h2>
      <Row size="md">
        <dl className="jh-entity-details">
          <dt>
            <Translate contentKey="shopManagement.id">id</Translate>
          </dt>
          <dd>
            <span>{shop.id}</span>&nbsp;
            {shop.activated ? (
              <Badge color="success">
                <Translate contentKey="shopManagement.activated">Activated</Translate>
              </Badge>
            ) : (
              <Badge color="danger">
                <Translate contentKey="shopManagement.deactivated">Deactivated</Translate>
              </Badge>
            )}
          </dd>
          <dt>
            <Translate contentKey="shopManagement.nameKo">Name Ko</Translate>
          </dt>
          <dd>{shop.nameKo}</dd>
          <dt>
            <Translate contentKey="shopManagement.nameEn">Name En</Translate>
          </dt>
          <dd>{shop.nameEn}</dd>
          <dt>
            <Translate contentKey="shopManagement.type">Type</Translate>
          </dt>
          <dd>{shop.type ? types.filter((type: any) => type.key === shop.type).map(type => type.name) : undefined}</dd>
          <dt>
            <Translate contentKey="shopManagement.workStartTime">근무시작시간</Translate>
          </dt>
          <dd>{shop.workStartTime}</dd>
          <dt>
            <Translate contentKey="shopManagement.createdBy">Created By</Translate>
          </dt>
          <dd>{shop.createdBy}</dd>
          <dt>
            <Translate contentKey="shopManagement.createdDate">Created Date</Translate>
          </dt>
          <dd>{shop.createdDate ? <TextFormat value={shop.createdDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid /> : null}</dd>
          <dt>
            <Translate contentKey="shopManagement.lastModifiedBy">Last Modified By</Translate>
          </dt>
          <dd>{shop.lastModifiedBy}</dd>
          <dt>
            <Translate contentKey="shopManagement.lastModifiedDate">Last Modified Date</Translate>
          </dt>
          <dd>
            {shop.lastModifiedDate ? (
              <TextFormat value={shop.lastModifiedDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
            ) : null}
          </dd>
        </dl>
      </Row>
      <Button tag={Link} to="/admin/shop-management" replace color="info">
        <FontAwesomeIcon icon="arrow-left" />{' '}
        <span className="d-none d-md-inline">
          <Translate contentKey="entity.action.back">Back</Translate>
        </span>
      </Button>
    </div>
  );
};

export default ShopManagementDetail;
