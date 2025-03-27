import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { isEmail, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { languages, locales } from 'app/config/translation';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createUser, getShopRoles, getUser, reset, updateUser } from './model-management.reducer';

export const ModelManagementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { shopId, userId } = useParams();

  const isNew = userId === undefined;

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getUser({ shopId, userId }));
    }
    dispatch(getShopRoles());
    return () => {
      dispatch(reset());
    };
  }, [userId, shopId]);

  const handleClose = () => {
    navigate('/shop/model-management');
  };

  const saveUser = values => {
    const entity = {
      ...values,
      shopId,
    };
    console.warn('entity', entity);
    if (isNew) {
      dispatch(createUser(entity));
    } else {
      dispatch(updateUser(entity));
    }
    handleClose();
  };

  const isInvalid = false;
  const user = useAppSelector(state => state.modelManagement.user);
  const loading = useAppSelector(state => state.modelManagement.loading);
  const updating = useAppSelector(state => state.modelManagement.updating);
  const authorities = useAppSelector(state => state.modelManagement.authorities);

  useEffect(() => {
    console.warn('user', user);
  }, [user]);

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h1>
            <Translate contentKey="userManagement.home.createOrEditLabel">Create or edit a User</Translate>
          </h1>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm onSubmit={saveUser} defaultValues={user}>
              {user.id ? (
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
                name="login"
                label={translate('userManagement.login')}
                validate={{
                  required: {
                    value: true,
                    message: translate('register.messages.validate.login.required'),
                  },
                  pattern: {
                    value: /^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$/,
                    message: translate('register.messages.validate.login.pattern'),
                  },
                  minLength: {
                    value: 1,
                    message: translate('register.messages.validate.login.minlength'),
                  },
                  maxLength: {
                    value: 50,
                    message: translate('register.messages.validate.login.maxlength'),
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="firstName"
                label={translate('userManagement.firstName')}
                validate={{
                  maxLength: {
                    value: 50,
                    message: translate('entity.validation.maxlength', { max: 50 }),
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="lastName"
                label={translate('userManagement.lastName')}
                validate={{
                  maxLength: {
                    value: 50,
                    message: translate('entity.validation.maxlength', { max: 50 }),
                  },
                }}
              />
              <ValidatedField
                type="text"
                name="modelNo"
                label={translate('userManagement.modelNo')}
                validate={{
                  maxLength: {
                    value: 50,
                    message: translate('entity.validation.maxlength', { max: 50 }),
                  },
                }}
              />
              <FormText>This field cannot be longer than 50 characters.</FormText>
              <ValidatedField
                name="email"
                label={translate('global.form.email.label')}
                placeholder={translate('global.form.email.placeholder')}
                type="email"
                validate={{
                  required: {
                    value: true,
                    message: translate('global.messages.validate.email.required'),
                  },
                  minLength: {
                    value: 5,
                    message: translate('global.messages.validate.email.minlength'),
                  },
                  maxLength: {
                    value: 254,
                    message: translate('global.messages.validate.email.maxlength'),
                  },
                  validate: v => isEmail(v) || translate('global.messages.validate.email.invalid'),
                }}
              />
              <ValidatedField
                type="checkbox"
                name="activated"
                check
                value={user.activated}
                disabled={!user.id}
                label={translate('userManagement.activated')}
              />
              <ValidatedField
                type="checkbox"
                name="commissionTargetYn"
                check
                value={user.isCommissionTargetYn}
                label={translate('userManagement.commissionTargetYn')}
              />
              <ValidatedField
                type="checkbox"
                name="discountAcceptYn"
                check
                value={user.isDiscountAcceptYn}
                label={translate('userManagement.discountAcceptYn')}
              />
              <ValidatedField
                type="select"
                name="langKey"
                label={translate('userManagement.langKey')}
                validate={{
                  required: {
                    value: true,
                    message: translate('global.messages.validate.langKey.required'),
                  },
                }}
              >
                {locales.map(locale => (
                  <option value={locale} key={locale}>
                    {languages[locale].name}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                type="select"
                name="authorities"
                multiple
                label={translate('userManagement.profiles')}
                validate={{
                  required: {
                    value: true,
                    message: translate('global.messages.validate.profiles.required'),
                  },
                }}
              >
                {authorities.map(role => (
                  <option value={role} key={role}>
                    {role}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} to="/shop/model-management" replace color="info">
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

export default ModelManagementUpdate;
