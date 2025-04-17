import React, { useEffect, useState } from 'react';
import { Translate, ValidatedField } from 'react-jhipster';
import { Alert, Button, Col, Form, Modal, ModalBody, ModalFooter, ModalHeader, Row } from 'reactstrap';
import { useForm } from 'react-hook-form';
import './order.scss';
import { IShopSalesItem } from 'app/shared/model/shopSalesItem.model';
import { IModelUser } from 'app/shared/model/modelUser.model';
import { IShopTable } from 'app/shared/model/shopTable.model';
import { useAppDispatch } from 'app/config/store';
import { deleteOrderDetail } from 'app/modules/order/order.reducer';
import { IOrderDetail } from 'app/shared/model/orderDetail.model';

export interface IOrderModalDiscountProps {
  showPayModal: boolean;
  orderError: boolean;
  handlePayOrder: (obj: any) => void;
  handlePayClose: () => void;
  orderDetails: IOrderDetail[];
  orderDetailWithDiscounts: IOrderDetail[];
  orderId: null;
}

const OrderModalDiscount = (props: IOrderModalDiscountProps) => {
  const orderHandlePayProps = obj => {
    props.handlePayOrder(obj);
  };

  const {
    handleSubmit,
    register,
    reset,
    watch,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const dispatch = useAppDispatch();

  const { orderError, handlePayClose } = props;

  const [leftItems, setLeftItems] = useState([]); // 왼쪽 div 아이템들
  const [draggedItem, setDraggedItem] = useState(null); // 드래그 중인 아이템

  const handleOrderPaySubmit = handleSubmit(data => {
    orderHandlePayProps(data);
    handlePayClose();
  });

  const handleOrderPayClose = () => {
    setLeftItems([]);
    handlePayClose();
  };

  const handleDragStart = item => {
    // console.warn('handleDragStart', item);
    setDraggedItem(item); // 드래그 시작 시 아이템 저장
  };

  const handleDragOver = e => {
    e.preventDefault(); // 기본 동작 방지 (drop 이벤트 허용)
  };

  const handleDrop = () => {
    if (draggedItem) {
      reset();
      setLeftItems(prev => [...prev, draggedItem]); // 왼쪽에 복사 추가
    }
  };

  const handlePayRemove = (index, item) => {
    setLeftItems(prev => prev.filter((_, i) => i !== index)); // 왼쪽에서 삭제
    // console.warn('handlePayRemove', item);
    if (item.salesItemId) {
      dispatch(deleteOrderDetail({ orderId: props.orderId, orderDetailId: item.id }));
    }
  };

  useEffect(() => {
    reset(); // react-hook-form 의 상태도 초기화
    setLeftItems([]); // 새로운 주문일 경우 leftItems 초기화
  }, [props.orderDetailWithDiscounts, props.orderId, reset]);

  // watch() 는 폼의 모든 값을 실시간으로 관찰합니다.
  const allValues = watch();

  useEffect(() => {
    // console.warn('Current Form Values:', allValues); // 값이 변경될 때마다 출력
  }, [allValues]);

  return (
    <Modal
      isOpen={props.showPayModal}
      toggle={handleOrderPayClose}
      backdrop="static"
      id="order-discount-page"
      autoFocus={false}
      className="modal-xl"
    >
      <Form onSubmit={handleOrderPaySubmit}>
        <ModalHeader id="order-title" data-cy="orderTitle" toggle={handlePayClose}>
          <Translate contentKey="global.menu.order">주문</Translate>
        </ModalHeader>
        <ModalBody>
          <Row>
            <Col md="12">
              <div className="mb-3"></div>

              <div className="d-flex gap-4">
                <div className="flex-grow-1 border rounded p-3 shadow-sm">
                  <h5 className="mb-3">
                    <Translate contentKey="order.label.details">주문 내역</Translate>
                  </h5>
                  <div className="mb-3 border rounded p-2 shadow-sm">
                    {props.orderDetails?.map((item, index) => (
                      <div key={index} className="flex justify-between items-center bg-gray-100 px-3 py-2 rounded">
                        <p>{item.nameKo}</p>
                        <p className="font-semibold">{item.price}</p>
                      </div>
                    ))}
                  </div>
                </div>

                <div className="flex-grow-1 border rounded p-3 shadow-sm" onDragOver={handleDragOver} onDrop={handleDrop}>
                  <h5 className="mb-3">
                    <Translate contentKey="order.label.selectedDiscounts">선택된 할인</Translate>
                  </h5>
                  {leftItems.map((item, index) => (
                    <div key={index} className="mb-3 border rounded p-2 shadow-sm">
                      <div className="d-flex justify-content-between align-items-center mb-2">
                        <p>{item.nameKo}</p>
                        <button className="text-red-500" onClick={() => handlePayRemove(index, item)}>
                          ✖
                        </button>
                      </div>
                      <ValidatedField
                        type="hidden"
                        register={register}
                        id={`salesItemIds[${index}]`}
                        name={`salesItemIds[${index}]`}
                        value={item.shopSalesItemId}
                      />
                      <ValidatedField
                        type="hidden"
                        register={register}
                        id={`salesItemDiscountIds[${index}]`}
                        name={`salesItemDiscountIds[${index}]`}
                        value={item.id}
                      />
                      <ValidatedField
                        type="hidden"
                        register={register}
                        id={`prices[${index}]`}
                        name={`prices[${index}]`}
                        value={item.price}
                      />
                    </div>
                  ))}
                </div>

                <div className="border rounded p-3 shadow-sm" style={{ width: '300px' }}>
                  <h5 className="mb-3">
                    <Translate contentKey="order.label.discountApplicable">적용 가능한 할인 항목</Translate>
                  </h5>
                  {props.orderDetailWithDiscounts.map((item, index) => (
                    <div
                      key={index}
                      className="border rounded p-2 mb-2 shadow-sm text-center cursor-pointer"
                      draggable
                      onDragStart={() => handleDragStart(item)}
                      style={{ cursor: 'grab' }}
                    >
                      {item.nameKo}
                    </div>
                  ))}
                  <ValidatedField type="hidden" register={register} id="orderId" name="orderId" value={props.orderId} />
                </div>
              </div>
            </Col>
          </Row>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={handleOrderPayClose} tabIndex={1}>
            <Translate contentKey="entity.action.cancel">Cancel</Translate>
          </Button>
          <Button color="primary" type="submit" data-cy="submit">
            <Translate contentKey="order.button.pay_processing">Payment Processing</Translate>
          </Button>
        </ModalFooter>
      </Form>
    </Modal>
  );
};

export default OrderModalDiscount;
