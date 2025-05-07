import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createSalesItemDiscount, getShopSalesItemDiscount, reset, updateSalesItemDiscount } from './sales-item-discount.reducer';
import { getShopSalesItems } from 'app/modules/order/order.reducer';

export const SalesItemDiscountUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { shopId, salesItemDiscountId } = useParams();
  const isNew = salesItemDiscountId === undefined;

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getShopSalesItemDiscount({ shopId, salesItemDiscountId }));
    }
    return () => {
      dispatch(reset());
    };
  }, [salesItemDiscountId]);

  const handleClose = () => {
    navigate('/shop/sales-item-discount');
  };

  const saveUser = values => {
    const entity = {
      ...values,
      shopId,
    };
    console.warn('entity', entity);
    if (isNew) {
      dispatch(createSalesItemDiscount(entity));
    } else {
      dispatch(updateSalesItemDiscount(entity));
    }
    handleClose();
  };

  const isInvalid = false;
  const salesItemDiscount = useAppSelector(state => state.salesItemDiscounts.salesItemDiscount);
  const loading = useAppSelector(state => state.salesItemDiscounts.loading);
  const updating = useAppSelector(state => state.salesItemDiscounts.updating);
  const salesItems = useAppSelector(state => state.orders.salesItems);

  const types: any = [
    { key: 'PRICE', nameKo: '금액', nameEn: 'Price' },
    { key: 'PERCENT', nameKo: '퍼센트', nameEn: 'Percent' },
  ];

  useEffect(() => {
    dispatch(getShopSalesItems({ shopId }));
  }, [shopId]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>
            <Translate contentKey="salesItemDiscount.home.createOrEditLabel">Create or edit a User</Translate>
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm onSubmit={saveUser} defaultValues={salesItemDiscount}>
              {salesItemDiscount.id ? (
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
                type="select"
                name="salesItemId"
                label={translate('salesItem.home.title')}
                validate={{
                  required: { value: true, message: translate('global.messages.validate.salesItem.required') },
                }}
              >
                {salesItems.map(type => (
                  <option value={type.id} key={type.id}>
                    {type.nameKo}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                type="text"
                name="nameKo"
                label={translate('global.label.nameKo')}
                validate={{
                  required: {
                    value: true,
                    message: translate('global.messages.validate.nameKo.required'),
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
                    message: translate('global.messages.validate.nameEn.required'),
                  },
                  maxLength: {
                    value: 60,
                    message: translate('entity.validation.maxlength', { max: 60 }),
                  },
                }}
              />
              <ValidatedField
                type="select"
                name="type"
                label={translate('salesItemDiscount.type')}
                validate={{
                  required: { value: true, message: translate('global.messages.validate.discountType.required') },
                }}
              >
                {types.map(type => (
                  <option value={type.key} key={type.key}>
                    {type.nameKo}
                  </option>
                ))}
              </ValidatedField>
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
                name="modelCommissionPrice"
                label={translate('global.label.modelCommissionPrice')}
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
              <Button tag={Link} to="/shop/sales-item-discount" replace color="info">
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

export default SalesItemDiscountUpdate;
