import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createBonusSalary, getShopBonusSalary, reset, updateBonusSalary } from './bonus-salary.reducer';
import { bonusTypes } from 'app/config/constants';

export const BonusSalaryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { shopId, shopBonusSalaryId } = useParams();
  const isNew = shopBonusSalaryId === undefined;

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getShopBonusSalary({ shopId, shopBonusSalaryId }));
    }
    return () => {
      dispatch(reset());
    };
  }, [shopBonusSalaryId]);

  const handleClose = () => {
    navigate('/shop/bonus-salary');
  };

  const saveUser = values => {
    const entity = {
      ...values,
      shopId,
    };
    if (isNew) {
      dispatch(createBonusSalary(entity));
    } else {
      dispatch(updateBonusSalary(entity));
    }
    handleClose();
  };

  const isInvalid = false;
  const bonusSalary = useAppSelector(state => state.bonusSalaries.bonusSalary);
  const loading = useAppSelector(state => state.bonusSalaries.loading);
  const updating = useAppSelector(state => state.bonusSalaries.updating);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>
            <Translate contentKey="bonusSalary.home.createOrEditLabel">보너스 생성 또는 수정</Translate>
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm onSubmit={saveUser} defaultValues={bonusSalary}>
              {bonusSalary.id ? (
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
                type="select"
                name="type"
                label={translate('bonusSalary.type')}
                validate={{
                  required: {
                    value: true,
                    message: translate('global.messages.validate.bonusType.required'),
                  },
                }}
              >
                {bonusTypes.map(type => (
                  <option value={type.key} key={type.key}>
                    {type.name}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                type="checkbox"
                name="activated"
                check
                value={true}
                disabled={!bonusSalary.id}
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
              <Button tag={Link} to="/shop/bonus-salary" replace color="info">
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

export default BonusSalaryUpdate;
