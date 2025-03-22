import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Badge, Button, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_TIMESTAMP_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getShopBaseSalary } from './base-salary.reducer';

export const BaseSalaryDetail = () => {
  const dispatch = useAppDispatch();

  const { shopId, shopBaseSalaryId } = useParams();

  useEffect(() => {
    dispatch(getShopBaseSalary({ shopId, shopBaseSalaryId }));
  }, []);

  const baseSalary = useAppSelector(state => state.baseSalaries.baseSalary);

  return (
    <div>
      <h2>
        <Translate contentKey="baseSalary.detail.title">벌금</Translate>
      </h2>
      <Row size="md">
        <dl className="jh-entity-details">
          <dt>
            <Translate contentKey="global.field.id">ID</Translate>
          </dt>
          <dd>
            <span>{baseSalary.id}</span>&nbsp;
            {baseSalary.activated ? (
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
          <dd>{baseSalary.nameKo}</dd>
          <dt>
            <Translate contentKey="global.label.nameEn">영문명</Translate>
          </dt>
          <dd>{baseSalary.nameEn}</dd>
          <dt>
            <Translate contentKey="global.label.price">금액</Translate>
          </dt>
          <dd>{baseSalary.price}</dd>
          <dt>
            <Translate contentKey="userManagement.createdBy">Created By</Translate>
          </dt>
          <dd>{baseSalary.createdBy}</dd>
          <dt>
            <Translate contentKey="userManagement.createdDate">Created Date</Translate>
          </dt>
          <dd>
            {baseSalary.createdDate ? (
              <TextFormat value={baseSalary.createdDate} type="date" format={APP_LOCAL_TIMESTAMP_FORMAT} blankOnInvalid />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="userManagement.lastModifiedBy">Last Modified By</Translate>
          </dt>
          <dd>{baseSalary.lastModifiedBy}</dd>
          <dt>
            <Translate contentKey="userManagement.lastModifiedDate">Last Modified Date</Translate>
          </dt>
          <dd>
            {baseSalary.lastModifiedDate ? (
              <TextFormat value={baseSalary.lastModifiedDate} type="date" format={APP_LOCAL_TIMESTAMP_FORMAT} blankOnInvalid />
            ) : null}
          </dd>
        </dl>
      </Row>
      <Button tag={Link} to="/shop/base-salary" replace color="info">
        <FontAwesomeIcon icon="arrow-left" />{' '}
        <span className="d-none d-md-inline">
          <Translate contentKey="entity.action.back">Back</Translate>
        </span>
      </Button>
    </div>
  );
};

export default BaseSalaryDetail;
