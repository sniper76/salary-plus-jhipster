import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Badge, Button, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_TIMESTAMP_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getShopSalesItemDiscount } from './sales-item-discount.reducer';

export const SalesItemDiscountDetail = () => {
  const dispatch = useAppDispatch();

  const { shopId, salesItemDiscountId } = useParams();

  useEffect(() => {
    dispatch(getShopSalesItemDiscount({ shopId, salesItemDiscountId }));
  }, []);

  const salesItemDiscount = useAppSelector(state => state.salesItemDiscounts.salesItemDiscount);

  return (
    <div>
      <h2>
        <Translate contentKey="salesItemDiscount.detail.title">할인</Translate>
      </h2>
      <Row size="md">
        <dl className="jh-entity-details">
          <dt>
            <Translate contentKey="global.field.id">ID</Translate>
          </dt>
          <dd>
            <span>{salesItemDiscount.id}</span>&nbsp;
            {salesItemDiscount.activated ? (
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
          <dd>{salesItemDiscount.nameKo}</dd>
          <dt>
            <Translate contentKey="global.label.nameEn">영문명</Translate>
          </dt>
          <dd>{salesItemDiscount.nameEn}</dd>
          <dt>
            <Translate contentKey="global.label.price">금액</Translate>
          </dt>
          <dd>{salesItemDiscount.price}</dd>
          <dt>
            <Translate contentKey="global.label.shopCommissionPrice">상점 커미션 금액</Translate>
          </dt>
          <dd>{salesItemDiscount.shopCommissionPrice}</dd>
          <dt>
            <Translate contentKey="global.label.mamaCommissionPrice">마마 커미션 금액</Translate>
          </dt>
          <dd>{salesItemDiscount.mamaCommissionPrice}</dd>
          <dt>
            <Translate contentKey="global.label.modelCommissionPrice">모델 커미션 금액</Translate>
          </dt>
          <dd>{salesItemDiscount.modelCommissionPrice}</dd>
          <dt>
            <Translate contentKey="userManagement.createdBy">Created By</Translate>
          </dt>
          <dd>{salesItemDiscount.createdBy}</dd>
          <dt>
            <Translate contentKey="userManagement.createdDate">Created Date</Translate>
          </dt>
          <dd>
            {salesItemDiscount.createdDate ? (
              <TextFormat value={salesItemDiscount.createdDate} type="date" format={APP_LOCAL_TIMESTAMP_FORMAT} blankOnInvalid />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="userManagement.lastModifiedBy">Last Modified By</Translate>
          </dt>
          <dd>{salesItemDiscount.lastModifiedBy}</dd>
          <dt>
            <Translate contentKey="userManagement.lastModifiedDate">Last Modified Date</Translate>
          </dt>
          <dd>
            {salesItemDiscount.lastModifiedDate ? (
              <TextFormat value={salesItemDiscount.lastModifiedDate} type="date" format={APP_LOCAL_TIMESTAMP_FORMAT} blankOnInvalid />
            ) : null}
          </dd>
        </dl>
      </Row>
      <Button tag={Link} to="/shop/sales-item-discount" replace color="info">
        <FontAwesomeIcon icon="arrow-left" />{' '}
        <span className="d-none d-md-inline">
          <Translate contentKey="entity.action.back">Back</Translate>
        </span>
      </Button>
    </div>
  );
};

export default SalesItemDiscountDetail;
