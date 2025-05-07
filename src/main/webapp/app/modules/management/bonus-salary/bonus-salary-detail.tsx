import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Badge, Button, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_TIMESTAMP_FORMAT, getBonusTypeName } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getShopBonusSalary } from './bonus-salary.reducer';

export const BonusSalaryDetail = () => {
  const dispatch = useAppDispatch();

  const { shopId, shopBonusSalaryId } = useParams();

  useEffect(() => {
    dispatch(getShopBonusSalary({ shopId, shopBonusSalaryId }));
  }, []);

  const bonusSalary = useAppSelector(state => state.bonusSalaries.bonusSalary);

  return (
    <div>
      <h2>
        <Translate contentKey="bonusSalary.detail.title">보너스</Translate>
      </h2>
      <Row size="md">
        <dl className="jh-entity-details">
          <dt>
            <Translate contentKey="global.label.nameKo">한글명</Translate>
          </dt>
          <dd>{bonusSalary.nameKo}</dd>
          <dt>
            <Translate contentKey="global.label.nameEn">영문명</Translate>
          </dt>
          <dd>{bonusSalary.nameEn}</dd>
          <dt>
            <Translate contentKey="bonusSalary.type">타입</Translate>
          </dt>
          <dd>{getBonusTypeName(bonusSalary.type)}</dd>
          <dt>
            <Translate contentKey="global.label.price">금액</Translate>
          </dt>
          <dd>{bonusSalary.price}</dd>
          <dt>
            <Translate contentKey="userManagement.createdBy">Created By</Translate>
          </dt>
          <dd>{bonusSalary.createdBy}</dd>
          <dt>
            <Translate contentKey="userManagement.createdDate">Created Date</Translate>
          </dt>
          <dd>
            {bonusSalary.createdDate ? (
              <TextFormat value={bonusSalary.createdDate} type="date" format={APP_LOCAL_TIMESTAMP_FORMAT} blankOnInvalid />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="userManagement.lastModifiedBy">Last Modified By</Translate>
          </dt>
          <dd>{bonusSalary.lastModifiedBy}</dd>
          <dt>
            <Translate contentKey="userManagement.lastModifiedDate">Last Modified Date</Translate>
          </dt>
          <dd>
            {bonusSalary.lastModifiedDate ? (
              <TextFormat value={bonusSalary.lastModifiedDate} type="date" format={APP_LOCAL_TIMESTAMP_FORMAT} blankOnInvalid />
            ) : null}
          </dd>
        </dl>
      </Row>
      <Button tag={Link} to="/shop/bonus-salary" replace color="info">
        <FontAwesomeIcon icon="arrow-left" />{' '}
        <span className="d-none d-md-inline">
          <Translate contentKey="entity.action.back">Back</Translate>
        </span>
      </Button>
    </div>
  );
};

export default BonusSalaryDetail;
