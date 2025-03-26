import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Badge, Button, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_TIMESTAMP_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getShopSalesItem } from './sales-item.reducer';

export const SalesItemDetail = () => {
  const dispatch = useAppDispatch();

  const { shopId, salesItemId } = useParams();

  useEffect(() => {
    dispatch(getShopSalesItem({ shopId, salesItemId }));
  }, []);

  const salesItem = useAppSelector(state => state.salesItems.salesItem);

  return (
    <div>
      <h2>
        <Translate contentKey="salesItem.detail.title">상품</Translate>
      </h2>
      <Row size="md">
        <dl className="jh-entity-details">
          <dt>
            <Translate contentKey="global.field.id">ID</Translate>
          </dt>
          <dd>
            <span>{salesItem.id}</span>&nbsp;
            {salesItem.activated ? (
              <Badge color="success">
                <Translate contentKey="userManagement.activated">Activated</Translate>
              </Badge>
            ) : (
              <Badge color="danger">
                <Translate contentKey="userManagement.deactivated">Deactivated</Translate>
              </Badge>
            )}
          </dd>
          <dt>
            <Translate contentKey="global.label.nameKo">한글명</Translate>
          </dt>
          <dd>{salesItem.nameKo}</dd>
          <dt>
            <Translate contentKey="global.label.nameEn">영문명</Translate>
          </dt>
          <dd>{salesItem.nameEn}</dd>
          <dt>
            <Translate contentKey="userManagement.commissionTargetYn">커미션 대상 여부</Translate>
          </dt>
          <dd>{salesItem.commissionTarget ? 'Yes' : 'No'}</dd>
          <dt>
            <Translate contentKey="salesItem.snackYn">안주 여부</Translate>
          </dt>
          <dd>{salesItem.snack ? 'Yes' : 'No'}</dd>
          <dt>
            <Translate contentKey="global.label.price">금액</Translate>
          </dt>
          <dd>{salesItem.price}</dd>
          <dt>
            <Translate contentKey="global.label.shopCommissionPrice">상점 커미션 금액</Translate>
          </dt>
          <dd>{salesItem.shopCommissionPrice}</dd>
          <dt>
            <Translate contentKey="global.label.mamaCommissionPrice">마마 커미션 금액</Translate>
          </dt>
          <dd>{salesItem.mamaCommissionPrice}</dd>
          <dt>
            <Translate contentKey="global.label.modelCommissionPrice">모델 커미션 금액</Translate>
          </dt>
          <dd>{salesItem.modelCommissionPrice}</dd>
          <dt>
            <Translate contentKey="userManagement.createdBy">Created By</Translate>
          </dt>
          <dd>{salesItem.createdBy}</dd>
          <dt>
            <Translate contentKey="userManagement.createdDate">Created Date</Translate>
          </dt>
          <dd>
            {salesItem.createdDate ? (
              <TextFormat value={salesItem.createdDate} type="date" format={APP_LOCAL_TIMESTAMP_FORMAT} blankOnInvalid />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="userManagement.lastModifiedBy">Last Modified By</Translate>
          </dt>
          <dd>{salesItem.lastModifiedBy}</dd>
          <dt>
            <Translate contentKey="userManagement.lastModifiedDate">Last Modified Date</Translate>
          </dt>
          <dd>
            {salesItem.lastModifiedDate ? (
              <TextFormat value={salesItem.lastModifiedDate} type="date" format={APP_LOCAL_TIMESTAMP_FORMAT} blankOnInvalid />
            ) : null}
          </dd>
        </dl>
      </Row>
      <Button tag={Link} to="/shop/sales-item" replace color="info">
        <FontAwesomeIcon icon="arrow-left" />{' '}
        <span className="d-none d-md-inline">
          <Translate contentKey="entity.action.back">Back</Translate>
        </span>
      </Button>
    </div>
  );
};

export default SalesItemDetail;
