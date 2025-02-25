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

  return (
    <div>
      <h2>
        <Translate contentKey="shopManagement.detail.title">User</Translate> [<strong>{shop.login}</strong>]
      </h2>
      <Row size="md">
        <dl className="jh-entity-details">
          <dt>
            <Translate contentKey="shopManagement.login">Login</Translate>
          </dt>
          <dd>
            <span>{shop.login}</span>&nbsp;
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
            <Translate contentKey="shopManagement.firstName">First Name</Translate>
          </dt>
          <dd>{shop.firstName}</dd>
          <dt>
            <Translate contentKey="shopManagement.lastName">Last Name</Translate>
          </dt>
          <dd>{shop.lastName}</dd>
          <dt>
            <Translate contentKey="shopManagement.email">Email</Translate>
          </dt>
          <dd>{shop.email}</dd>
          <dt>
            <Translate contentKey="shopManagement.langKey">Lang Key</Translate>
          </dt>
          <dd>{shop.langKey ? languages[shop.langKey].name : undefined}</dd>
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
          <dt>
            <Translate contentKey="shopManagement.profiles">Profiles</Translate>
          </dt>
          <dd>
            <ul className="list-unstyled">
              {shop.authorities
                ? shop.authorities.map((authority, i) => (
                    <li key={`shop-auth-${i}`}>
                      <Badge color="info">{authority}</Badge>
                    </li>
                  ))
                : null}
            </ul>
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
