import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deleteBaseSalary, getShopBaseSalary } from './base-salary.reducer';

export const BaseSalaryDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { shopId, shopBaseSalaryId } = useParams();

  useEffect(() => {
    dispatch(getShopBaseSalary({ shopId, shopBaseSalaryId }));
  }, []);

  const baseSalary = useAppSelector(state => state.baseSalaries.baseSalary);

  const handleClose = event => {
    event.stopPropagation();
    navigate('/shop/base-salary');
  };

  const confirmDelete = event => {
    dispatch(deleteBaseSalary({ shopId, shopBaseSalaryId }));
    handleClose(event);
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody>
        <Translate contentKey="baseSalary.delete.question" interpolate={{ name: baseSalary.nameKo }}>
          정말로 일당를 삭제하시겠습니까?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">취소</Translate>
        </Button>
        <Button color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">삭제</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default BaseSalaryDeleteDialog;
