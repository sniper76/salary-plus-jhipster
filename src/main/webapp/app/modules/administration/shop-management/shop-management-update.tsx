import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createShop, getShop, reset, updateShop } from './shop-management.reducer';

export const ShopManagementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getShop(id));
    }
    return () => {
      dispatch(reset());
    };
  }, [id]);

  const handleClose = () => {
    navigate('/admin/shop-management');
  };

  const saveShop = values => {
    if (isNew) {
      dispatch(createShop(values));
    } else {
      dispatch(updateShop(values));
    }
    handleClose();
  };

  const isInvalid = false;
  const shop = useAppSelector(state => state.shopManagement.shop);
  console.warn('shop', shop);
  const loading = useAppSelector(state => state.shopManagement.loading);
  const updating = useAppSelector(state => state.shopManagement.updating);

  const types: any = [
    { key: 'BAR', name: '바' },
    { key: 'RESTAURANT', name: '식당' },
  ];

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>
            <Translate contentKey="shopManagement.home.createOrEditLabel">Create or edit a User</Translate>
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm onSubmit={saveShop} defaultValues={shop}>
              {shop.id ? (
                <ValidatedField
                  type="text"
                  name="id"
                  required
                  readOnly
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                type="text"
                name="nameKo"
                label={translate('shopManagement.nameKo')}
                validate={{
                  maxLength: {
                    value: 50,
                    message: translate('entity.validation.maxlength', { max: 50 }),
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="nameEn"
                label={translate('shopManagement.nameEn')}
                validate={{
                  maxLength: {
                    value: 50,
                    message: translate('entity.validation.maxlength', { max: 50 }),
                  },
                }}
              />
              <ValidatedField type="select" name="type" label={translate('shopManagement.type')}>
                {types.map(type => (
                  <option value={type.key} key={type.key}>
                    {type.name}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                type="text"
                name="workStartTime"
                label={translate('shopManagement.workStartTime')}
                validate={{
                  maxLength: {
                    value: 8,
                    message: translate('entity.validation.maxlength', { max: 8 }),
                  },
                }}
              />
              <Button tag={Link} to="/admin/shop-management" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" type="submit" disabled={isInvalid || updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ShopManagementUpdate;
