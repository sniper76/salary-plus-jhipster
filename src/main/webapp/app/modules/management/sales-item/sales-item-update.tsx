import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createSalesItem, getShopSalesItem, reset, updateSalesItem } from './sales-item.reducer';

export const SalesItemUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { shopId, salesItemId } = useParams();
  const isNew = salesItemId === undefined;

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getShopSalesItem({ shopId, salesItemId }));
    }
    return () => {
      dispatch(reset());
    };
  }, [salesItemId]);

  const handleClose = () => {
    navigate('/shop/sales-item');
  };

  const saveUser = values => {
    const entity = {
      ...values,
      shopId,
    };
    if (isNew) {
      dispatch(createSalesItem(entity));
    } else {
      dispatch(updateSalesItem(entity));
    }
    handleClose();
  };

  const isInvalid = false;
  const salesItem = useAppSelector(state => state.salesItems.salesItem);
  const loading = useAppSelector(state => state.salesItems.loading);
  const updating = useAppSelector(state => state.salesItems.updating);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>
            <Translate contentKey="salesItem.home.createOrEditLabel">Create or edit a User</Translate>
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm onSubmit={saveUser} defaultValues={salesItem}>
              {salesItem.id ? (
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
                label={translate('global.label.nameKo')}
                validate={{
                  required: {
                    value: true,
                    message: translate('register.messages.validate.login.required'),
                  },
                  maxLength: {
                    value: 60,
                    message: translate('entity.validation.maxlength', { max: 60 }),
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="nameEn"
                label={translate('global.label.nameEn')}
                validate={{
                  required: {
                    value: true,
                    message: translate('register.messages.validate.login.required'),
                  },
                  maxLength: {
                    value: 60,
                    message: translate('entity.validation.maxlength', { max: 60 }),
                  },
                }}
              />
              <ValidatedField
                type="checkbox"
                name="commissionTargetYn"
                check
                value={salesItem.isCommissionTargetYn}
                label={translate('userManagement.commissionTargetYn')}
              />
              <ValidatedField type="checkbox" name="snackYn" check value={salesItem.isSnackYn} label={translate('salesItem.snackYn')} />
              <ValidatedField
                type="number"
                name="price"
                label={translate('global.label.price')}
                validate={{
                  required: {
                    value: true,
                    message: translate('register.messages.validate.login.required'),
                  },
                  min: { value: 1, message: 'Price must be a positive number.' },
                }}
              />
              <ValidatedField
                type="number"
                name="shopCommissionPrice"
                label={translate('global.label.shopCommissionPrice')}
                validate={{
                  required: {
                    value: true,
                    message: translate('register.messages.validate.login.required'),
                  },
                  min: { value: 1, message: 'Price must be a positive number.' },
                }}
              />
              <ValidatedField
                type="number"
                name="mamaCommissionPrice"
                label={translate('global.label.mamaCommissionPrice')}
                validate={{
                  required: {
                    value: true,
                    message: translate('register.messages.validate.login.required'),
                  },
                  min: { value: 0, message: 'Price must be a positive number.' },
                }}
              />
              <ValidatedField
                type="number"
                name="modelCommissionPrice"
                label={translate('global.label.modelCommissionPrice')}
                validate={{
                  required: {
                    value: true,
                    message: translate('register.messages.validate.login.required'),
                  },
                  min: { value: 0, message: 'Price must be a positive number.' },
                }}
              />
              <Button tag={Link} to="/shop/sales-item" replace color="info">
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

export default SalesItemUpdate;
