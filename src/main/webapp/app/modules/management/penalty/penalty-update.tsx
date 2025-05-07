import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createPenalty, getShopPenalty, reset, updatePenalty } from './penalty.reducer';
import { penaltyTypes } from 'app/config/constants';

export const PenaltyUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { shopId, shopPenaltyId } = useParams();
  const isNew = shopPenaltyId === undefined;

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getShopPenalty({ shopId, shopPenaltyId }));
    }
    return () => {
      dispatch(reset());
    };
  }, [shopPenaltyId]);

  const handleClose = () => {
    navigate('/shop/penalty');
  };

  const saveUser = values => {
    const entity = {
      ...values,
      shopId,
    };
    if (isNew) {
      dispatch(createPenalty(entity));
    } else {
      dispatch(updatePenalty(entity));
    }
    handleClose();
  };

  const isInvalid = false;
  const penalty = useAppSelector(state => state.penalties.penalty);
  const loading = useAppSelector(state => state.penalties.loading);
  const updating = useAppSelector(state => state.penalties.updating);

  const [selectTypeValue, setSelectTypeValue] = useState(penalty.type);

  const handleOnChangeType = e => {
    console.warn('handleOnChangeType', e.target.value);
    setSelectTypeValue(e.target.value);
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>
            <Translate contentKey="penalty.home.createOrEditLabel">벌금 생성 또는 수정</Translate>
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm onSubmit={saveUser} defaultValues={penalty}>
              {penalty.id ? (
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
              <ValidatedField type="select" name="type" label={translate('penalty.type')} onChange={handleOnChangeType}>
                {penaltyTypes.map(type => (
                  <option value={type.key} key={type.key}>
                    {type.name}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                type="text"
                name="typeValue"
                label={translate('penalty.typeValue')}
                validate={{
                  required: {
                    value: true,
                    message: translate('penalty.messages.validate.required.typeValue'),
                  },
                  maxLength: {
                    value: 60,
                    message: translate('entity.validation.maxlength', { max: 60 }),
                  },
                  pattern: {
                    value:
                      selectTypeValue === 'TIME'
                        ? /^([1-9]|[1-5]\d|60)[MH]$/
                        : /^(MON|TUE|WED|THU|FRI|SAT|SUN)(,(MON|TUE|WED|THU|FRI|SAT|SUN))*$/,
                    message:
                      selectTypeValue === 'TIME'
                        ? translate('penalty.messages.validate.pattern.typeValue.time')
                        : translate('penalty.messages.validate.pattern.typeValue.day'),
                  },
                }}
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
              <Button tag={Link} to="/shop/penalty" replace color="info">
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

export default PenaltyUpdate;
