import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { deleteSalesItem, getShopSalesItem } from './sales-item.reducer';

export const SalesItemDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { shopId, salesItemId } = useParams();

  useEffect(() => {
    dispatch(getShopSalesItem({ shopId, salesItemId }));
  }, []);

  const salesItem = useAppSelector(state => state.salesItems.salesItem);

  const handleClose = event => {
    event.stopPropagation();
    navigate('/shop/sales-item');
  };

  const confirmDelete = event => {
    dispatch(deleteSalesItem({ shopId, salesItemId }));
    handleClose(event);
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose}>
        <Translate contentKey="entity.delete.title">Confirm delete operation</Translate>
      </ModalHeader>
      <ModalBody>
        <Translate contentKey="salesItem.delete.question" interpolate={{ name: salesItem.nameKo }}>
          정말로 상품를 삭제하시겠습니까?
        </Translate>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp;
          <Translate contentKey="entity.action.cancel">Cancel</Translate>
        </Button>
        <Button color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp;
          <Translate contentKey="entity.action.delete">Delete</Translate>
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default SalesItemDeleteDialog;
