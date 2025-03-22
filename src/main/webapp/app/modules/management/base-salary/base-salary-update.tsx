import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createBaseSalary, getShopBaseSalary, reset, updateBaseSalary } from './base-salary.reducer';

export const BaseSalaryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { shopId, shopBaseSalaryId } = useParams();
  const isNew = shopBaseSalaryId === undefined;

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getShopBaseSalary({ shopId, shopBaseSalaryId }));
    }
    return () => {
      dispatch(reset());
    };
  }, [shopBaseSalaryId]);

  const handleClose = () => {
    navigate('/shop/base-salary');
  };

  const saveUser = values => {
    const entity = {
      ...values,
      shopId,
    };
    if (isNew) {
      dispatch(createBaseSalary(entity));
    } else {
      dispatch(updateBaseSalary(entity));
    }
    handleClose();
  };

  const isInvalid = false;
  const baseSalary = useAppSelector(state => state.baseSalaries.baseSalary);
  const loading = useAppSelector(state => state.baseSalaries.loading);
  const updating = useAppSelector(state => state.baseSalaries.updating);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>
            <Translate contentKey="baseSalary.home.createOrEditLabel">일당 생성 또는 수정</Translate>
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm onSubmit={saveUser} defaultValues={baseSalary}>
              {baseSalary.id ? (
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
                type="checkbox"
                name="activated"
                check
                value={true}
                disabled={!baseSalary.id}
                label={translate('userManagement.activated')}
              />
              <ValidatedField
                type="number"
                name="price"
                label={translate('global.label.price')}
                validate={{
                  required: {
                    value: true,
                    message: translate('global.messages.validate.price.required'),
                  },
                  min: { value: 1, message: translate('global.messages.validate.price.size') },
                }}
              />
              <Button tag={Link} to="/shop/base-salary" replace color="info">
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

export default BaseSalaryUpdate;
