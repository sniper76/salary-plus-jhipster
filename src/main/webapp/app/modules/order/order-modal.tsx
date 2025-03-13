import React, { useState, useEffect } from 'react';
import { Translate, ValidatedField } from 'react-jhipster';
import { Button, Col, Form, Modal, ModalBody, ModalFooter, ModalHeader, Row } from 'reactstrap';
import { useForm } from 'react-hook-form';
import { languages, locales } from 'app/config/translation';
import './order.scss';
import { IShopSalesItem } from 'app/shared/model/shopSalesItem.model';
import { IModelUser } from 'app/shared/model/modelUser.model';
import { IShopTable } from 'app/shared/model/shopTable.model';

export interface IOrderModalProps {
  showModal: boolean;
  orderError: boolean;
  handleOrder: (obj: any) => void;
  handleClose: () => void;
  salesItems: ReadonlyArray<IShopSalesItem>;
  models: ReadonlyArray<IModelUser>;
  tables: ReadonlyArray<IShopTable>;
  orderDetails: [];
}

const OrderModal = (props: IOrderModalProps) => {
  const orderHandleProps = obj => {
    props.handleOrder(obj);
  };

  const {
    handleSubmit,
    register,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const { orderError, handleClose } = props;

  const [leftItems, setLeftItems] = useState([]); // 왼쪽 div 아이템들
  const [draggedItem, setDraggedItem] = useState(null); // 드래그 중인 아이템

  const handleOrderSubmit = e => {
    console.warn('handleOrderSubmit', e, leftItems);
    handleSubmit(orderHandleProps)(e);
    setLeftItems([]);
    handleClose();
  };

  const handleDragStart = item => {
    setDraggedItem(item); // 드래그 시작 시 아이템 저장
  };

  const handleDragOver = e => {
    e.preventDefault(); // 기본 동작 방지 (drop 이벤트 허용)
  };

  const handleDrop = () => {
    if (draggedItem) {
      setLeftItems(prev => [...prev, draggedItem]); // 왼쪽에 복사 추가
    }
  };

  const handleRemove = (index, item) => {
    setLeftItems(prev => prev.filter((_, i) => i !== index)); // 왼쪽에서 삭제
    console.warn('handleRemove', item);
  };

  useEffect(() => {
    if (props.orderDetails) {
      console.warn('useEffect', props.orderDetails);
      setLeftItems(props.orderDetails);
    }
  }, [props]);

  return (
    <Modal isOpen={props.showModal} toggle={handleClose} backdrop="static" id="order-page" autoFocus={false}>
      <Form onSubmit={handleOrderSubmit}>
        <ModalHeader id="order-title" data-cy="orderTitle" toggle={handleClose}>
          <Translate contentKey="global.menu.order">Order</Translate>
        </ModalHeader>
        <ModalBody>
          <Row>
            <Col md="12">
              <div className="container">
                <ValidatedField className="select-box" type="select" register={register} id="tableId" name="tableId" data-cy="tableId">
                  {props.tables.map((model, idx) => (
                    <option value={model.id} key={idx}>
                      {model.no}
                    </option>
                  ))}
                </ValidatedField>
              </div>
              <div className="container">
                {/* 왼쪽 Div (드롭 가능) */}
                <div id="left" className="box" onDragOver={handleDragOver} onDrop={handleDrop}>
                  {leftItems.map((item, index) => (
                    <div key={index} className="item">
                      <div className="item-header">
                        <span className="item-name">
                          {item.nameKo}
                          <ValidatedField
                            type="hidden"
                            register={register}
                            id={`salesItemIds[${index}]`}
                            name={`salesItemIds[${index}]`}
                            data-cy="salesItemIds"
                            value={item.id}
                          />
                        </span>
                        <button className="close-btn" onClick={() => handleRemove(index, item)}>
                          ✖
                        </button>
                      </div>
                      {item.commissionTarget && (
                        <ValidatedField
                          className="select-box"
                          register={register}
                          type="select"
                          id={`modelIds[${index}]`}
                          name={`modelIds[${index}]`}
                          data-cy="modelIds"
                        >
                          {props.models.map((model, idx) => (
                            <option value={model.id} key={idx}>
                              {model.modelNo}
                            </option>
                          ))}
                        </ValidatedField>
                      )}
                      {item.snack ? (
                        <ValidatedField
                          register={register}
                          type="number"
                          id={`prices[${index}]`}
                          name={`prices[${index}]`}
                          validate={{
                            required: { value: true, message: 'Price is required.' },
                            min: { value: 0, message: 'Price must be a positive number.' },
                          }}
                          data-cy="prices"
                          value={item.price}
                        />
                      ) : (
                        <ValidatedField
                          register={register}
                          type="hidden"
                          id={`prices[${index}]`}
                          name={`prices[${index}]`}
                          data-cy="prices"
                          value={item.price}
                        />
                      )}
                    </div>
                  ))}
                </div>

                {/* 오른쪽 Div (드래그 가능) */}
                <div id="right" className="box">
                  {props.salesItems.map((item, index) => (
                    <div key={index} className="item" draggable onDragStart={() => handleDragStart(item)}>
                      {item.nameKo}
                    </div>
                  ))}
                </div>
              </div>
            </Col>
          </Row>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={handleClose} tabIndex={1}>
            <Translate contentKey="entity.action.cancel">Cancel</Translate>
          </Button>{' '}
          <Button color="primary" type="submit" data-cy="submit">
            <Translate contentKey="order.form.button">Ordering</Translate>
          </Button>
        </ModalFooter>
      </Form>
    </Modal>
  );
};

export default OrderModal;
