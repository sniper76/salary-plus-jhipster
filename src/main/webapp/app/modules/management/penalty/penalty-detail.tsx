import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Badge, Button, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_TIMESTAMP_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getShopPenalty } from './penalty.reducer';

export const PenaltyDetail = () => {
  const dispatch = useAppDispatch();

  const { shopId, shopPenaltyId } = useParams();

  useEffect(() => {
    dispatch(getShopPenalty({ shopId, shopPenaltyId }));
  }, []);

  const penalty = useAppSelector(state => state.penalties.penalty);

  return (
    <div>
      <h2>
        <Translate contentKey="penalty.detail.title">벌금</Translate>
      </h2>
      <Row size="md">
        <dl className="jh-entity-details">
          <dt>
            <Translate contentKey="global.field.id">ID</Translate>
          </dt>
          <dd>
            <span>{penalty.id}</span>&nbsp;
            {penalty.activated ? (
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
          <dd>{penalty.nameKo}</dd>
          <dt>
            <Translate contentKey="global.label.nameEn">영문명</Translate>
          </dt>
          <dd>{penalty.nameEn}</dd>
          <dt>
            <Translate contentKey="global.label.price">금액</Translate>
          </dt>
          <dd>{penalty.price}</dd>
          <dt>
            <Translate contentKey="penalty.type">벌금유형</Translate>
          </dt>
          <dd>{penalty.type}</dd>
          <dt>
            <Translate contentKey="penalty.typeValue">벌금유형값</Translate>
          </dt>
          <dd>{penalty.typeValue}</dd>
          <dt>
            <Translate contentKey="userManagement.createdBy">Created By</Translate>
          </dt>
          <dd>{penalty.createdBy}</dd>
          <dt>
            <Translate contentKey="userManagement.createdDate">Created Date</Translate>
          </dt>
          <dd>
            {penalty.createdDate ? (
              <TextFormat value={penalty.createdDate} type="date" format={APP_LOCAL_TIMESTAMP_FORMAT} blankOnInvalid />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="userManagement.lastModifiedBy">Last Modified By</Translate>
          </dt>
          <dd>{penalty.lastModifiedBy}</dd>
          <dt>
            <Translate contentKey="userManagement.lastModifiedDate">Last Modified Date</Translate>
          </dt>
          <dd>
            {penalty.lastModifiedDate ? (
              <TextFormat value={penalty.lastModifiedDate} type="date" format={APP_LOCAL_TIMESTAMP_FORMAT} blankOnInvalid />
            ) : null}
          </dd>
        </dl>
      </Row>
      <Button tag={Link} to="/shop/penalty" replace color="info">
        <FontAwesomeIcon icon="arrow-left" />{' '}
        <span className="d-none d-md-inline">
          <Translate contentKey="entity.action.back">Back</Translate>
        </span>
      </Button>
    </div>
  );
};

export default PenaltyDetail;
