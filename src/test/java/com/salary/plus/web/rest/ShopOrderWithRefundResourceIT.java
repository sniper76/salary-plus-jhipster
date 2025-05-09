package com.salary.plus.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shiver.me.timbers.data.random.RandomLongs.somePositiveLong;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.salary.plus.IntegrationTest;
import com.salary.plus.domain.Shop;
import com.salary.plus.domain.ShopOrder;
import com.salary.plus.domain.ShopOrderDetail;
import com.salary.plus.domain.ShopOrderDetailDiscount;
import com.salary.plus.domain.ShopSalesItem;
import com.salary.plus.domain.ShopSalesItemDiscount;
import com.salary.plus.domain.ShopSalesRefund;
import com.salary.plus.domain.ShopTable;
import com.salary.plus.domain.ShopUserSalesSalary;
import com.salary.plus.enums.DiscountType;
import com.salary.plus.enums.ShopType;
import com.salary.plus.repository.ShopOrderDetailDiscountRepository;
import com.salary.plus.repository.ShopOrderDetailRepository;
import com.salary.plus.repository.ShopOrderRepository;
import com.salary.plus.repository.ShopRepository;
import com.salary.plus.repository.ShopSalesItemDiscountRepository;
import com.salary.plus.repository.ShopSalesItemRepository;
import com.salary.plus.repository.ShopSalesRefundRepository;
import com.salary.plus.repository.ShopTableRepository;
import com.salary.plus.repository.ShopUserSalesSalaryRepository;
import com.salary.plus.security.AuthoritiesConstants;
import com.salary.plus.service.dto.ShopOrderCreateDTO;
import com.salary.plus.service.dto.ShopOrderDetailResponse;
import com.salary.plus.service.dto.ShopOrderRefundCreateDTO;
import com.salary.plus.utils.DateTimeFormatUtil;
import com.salary.plus.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@AutoConfigureMockMvc
@WithMockUser(authorities = AuthoritiesConstants.ADMIN)
@IntegrationTest
class ShopOrderWithRefundResourceIT {

    private static final String TARGET_REFUND_URL = "/api/shops/{shopId}/dates/{date}/orders/{orderId}/refunds";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopTableRepository shopTableRepository;

    @Autowired
    private ShopSalesItemRepository shopSalesItemRepository;

    @Autowired
    private ShopOrderRepository shopOrderRepository;

    @Autowired
    private ShopOrderDetailRepository shopOrderDetailRepository;

    @Autowired
    private ShopUserSalesSalaryRepository shopUserSalesSalaryRepository;

    @Autowired
    private ShopSalesItemDiscountRepository shopSalesItemDiscountRepository;

    @Autowired
    private ShopOrderDetailDiscountRepository shopOrderDetailDiscountRepository;

    @Autowired
    private ShopSalesRefundRepository shopSalesRefundRepository;

    @Autowired
    private MockMvc restUserMockMvc;

    @Nested
    class WhenSuccess {

        private Long shopId;
        private String date;
        private Long orderId;
        private Long tableId;
        private List<Long> salesItemIds;
        private List<Long> modelIds;
        private List<Integer> prices;
        private List<Long> orderDetailIds;

        private Integer longTimeBarFinePrice = 4000;
        private Integer ladyDrinkPrice = 350;
        private Integer guestDrinkPrice = 150;
        private Integer totalPrice = longTimeBarFinePrice + ladyDrinkPrice + guestDrinkPrice;
        private Integer snackPrice = 10000;

        private Long userId1;
        private Long userId2;

        private void mockData() {
            Shop shop = new Shop();
            shop.setType(ShopType.BAR);
            shop.setNameKo("shop1");
            shop.setNameEn("shop1");
            shop.setWorkStartTime("09:00:00");
            shopRepository.saveAndFlush(shop);

            shopId = shop.getId();

            ShopTable shopTable = new ShopTable();
            shopTable.setShopId(shopId);
            shopTable.setNo("T01");
            shopTableRepository.saveAndFlush(shopTable);

            tableId = shopTable.getId();

            ShopSalesItem shopSalesItem1 = new ShopSalesItem();
            shopSalesItem1.setShopId(shopId);
            shopSalesItem1.setNameKo("item1 ko");
            shopSalesItem1.setNameEn("item1 en");
            shopSalesItem1.setPrice(longTimeBarFinePrice);
            shopSalesItem1.setCommissionTargetYn(true);
            shopSalesItem1.setShopCommissionPrice(0);
            shopSalesItem1.setMamaCommissionPrice(0);
            shopSalesItem1.setModelCommissionPrice(0);
            shopSalesItem1.setSnackYn(false);
            shopSalesItemRepository.saveAndFlush(shopSalesItem1);

            ShopSalesItem shopSalesItem2 = new ShopSalesItem();
            shopSalesItem2.setShopId(shopId);
            shopSalesItem2.setNameKo("item2 ko");
            shopSalesItem2.setNameEn("item2 en");
            shopSalesItem2.setPrice(ladyDrinkPrice);
            shopSalesItem2.setCommissionTargetYn(true);
            shopSalesItem2.setShopCommissionPrice(0);
            shopSalesItem2.setMamaCommissionPrice(0);
            shopSalesItem2.setModelCommissionPrice(0);
            shopSalesItem2.setSnackYn(false);
            shopSalesItemRepository.saveAndFlush(shopSalesItem2);

            ShopSalesItem shopSalesItem3 = new ShopSalesItem();
            shopSalesItem3.setShopId(shopId);
            shopSalesItem3.setNameKo("item3 ko");
            shopSalesItem3.setNameEn("item3 en");
            shopSalesItem3.setPrice(guestDrinkPrice);
            shopSalesItem3.setCommissionTargetYn(false);
            shopSalesItem3.setShopCommissionPrice(0);
            shopSalesItem3.setMamaCommissionPrice(0);
            shopSalesItem3.setModelCommissionPrice(0);
            shopSalesItem3.setSnackYn(false);
            shopSalesItemRepository.saveAndFlush(shopSalesItem3);

            date = DateUtils.getFormatted(DateTimeFormatUtil.yyyy_MM_dd());
            salesItemIds = new ArrayList<>();
            salesItemIds.add(shopSalesItem1.getId());
            salesItemIds.add(shopSalesItem2.getId());
            salesItemIds.add(shopSalesItem3.getId());

            prices = new ArrayList<>();
            prices.add(shopSalesItem1.getPrice());
            prices.add(shopSalesItem2.getPrice());
            prices.add(shopSalesItem3.getPrice());

            userId1 = somePositiveLong();
            userId2 = somePositiveLong();
            modelIds = new ArrayList<>();
            modelIds.add(userId1);
            modelIds.add(userId2);
        }

        private void mockOrderData() {
            ShopOrder shopOrder = new ShopOrder();
            shopOrder.setShopId(shopId);
            shopOrder.setShopTableId(tableId);
            shopOrder.setPaid(false);
            shopOrder.setDate(date);
            shopOrder.setTotalPrice(totalPrice);
            shopOrder.setDiscountPrice(0);
            shopOrder.setRefundPrice(0);
            shopOrderRepository.saveAndFlush(shopOrder);

            orderId = shopOrder.getId();

            ShopOrderDetail shopOrderDetail1 = new ShopOrderDetail();
            shopOrderDetail1.setShopOrderId(shopOrder.getId());
            shopOrderDetail1.setShopSalesItemId(salesItemIds.get(0));
            shopOrderDetail1.setPrice(longTimeBarFinePrice);
            shopOrderDetailRepository.saveAndFlush(shopOrderDetail1);

            ShopOrderDetail shopOrderDetail2 = new ShopOrderDetail();
            shopOrderDetail2.setShopOrderId(shopOrder.getId());
            shopOrderDetail2.setShopSalesItemId(salesItemIds.get(1));
            shopOrderDetail2.setPrice(ladyDrinkPrice);
            shopOrderDetailRepository.saveAndFlush(shopOrderDetail2);

            ShopOrderDetail shopOrderDetail3 = new ShopOrderDetail();
            shopOrderDetail3.setShopOrderId(shopOrder.getId());
            shopOrderDetail3.setShopSalesItemId(salesItemIds.get(2));
            shopOrderDetail3.setPrice(guestDrinkPrice);
            shopOrderDetailRepository.saveAndFlush(shopOrderDetail3);

            orderDetailIds = new ArrayList<>();
            orderDetailIds.add(shopOrderDetail1.getId());
            orderDetailIds.add(shopOrderDetail2.getId());
            orderDetailIds.add(shopOrderDetail3.getId());

            ShopUserSalesSalary shopUserSalesSalary1 = new ShopUserSalesSalary();
            shopUserSalesSalary1.setShopOrderDetailId(shopOrderDetail1.getId());
            shopUserSalesSalary1.setUserId(userId1);
            shopUserSalesSalary1.setPrice(longTimeBarFinePrice);
            shopUserSalesSalaryRepository.saveAndFlush(shopUserSalesSalary1);

            ShopUserSalesSalary shopUserSalesSalary2 = new ShopUserSalesSalary();
            shopUserSalesSalary2.setShopOrderDetailId(shopOrderDetail2.getId());
            shopUserSalesSalary2.setUserId(userId2);
            shopUserSalesSalary2.setPrice(ladyDrinkPrice);
            shopUserSalesSalaryRepository.saveAndFlush(shopUserSalesSalary2);
        }

        @DisplayName("환불이 적용된 경우")
        @Nested
        class WhenUpdateWithRefunds {

            @DisplayName("변경된 정보가 없는 경우")
            @Nested
            class WhenNotChangeData {

                private Long longTimeBarFineSalesItemId;
                private Long longTimeBarFineOrderDetailId;
                private Long longTimeBarFineSalesItemDiscountId;
                private Integer longTimeBarFineSalesItemDiscountPrice;

                @BeforeEach
                void setUp() {
                    mockData();
                    mockOrderData();

                    longTimeBarFineSalesItemId = salesItemIds.get(0);
                    longTimeBarFineOrderDetailId = orderDetailIds.get(0);
                    longTimeBarFineSalesItemDiscountPrice = 500;

                    ShopSalesItemDiscount shopSalesItemDiscount = new ShopSalesItemDiscount();
                    shopSalesItemDiscount.setShopSalesItemId(longTimeBarFineSalesItemId);
                    shopSalesItemDiscount.setType(DiscountType.PRICE);
                    shopSalesItemDiscount.setNameKo("바파인 500");
                    shopSalesItemDiscount.setNameEn("bar fine discount 500");
                    shopSalesItemDiscount.setPrice(longTimeBarFineSalesItemDiscountPrice);
                    shopSalesItemDiscount.setShopCommissionPrice(300);
                    shopSalesItemDiscount.setModelCommissionPrice(200);
                    shopSalesItemDiscount.setMamaCommissionPrice(0);
                    shopSalesItemDiscountRepository.save(shopSalesItemDiscount);

                    longTimeBarFineSalesItemDiscountId = shopSalesItemDiscount.getId();
                }

                @DisplayName("정상 등록된다.")
                @Test
                @Transactional
                void create() throws Exception {
                    final int shopPrice = 500;
                    final int modelPrice = 300;
                    final int mamaPrice = 200;

                    // Create the User
                    ShopOrderRefundCreateDTO createDTO = new ShopOrderRefundCreateDTO();
                    createDTO.setShopPrice(shopPrice);
                    createDTO.setModelPrice(modelPrice);
                    createDTO.setMamaPrice(mamaPrice);

                    restUserMockMvc
                        .perform(
                            post(TARGET_REFUND_URL, shopId, date, orderId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsBytes(createDTO))
                        )
                        .andExpect(status().isCreated());

                    final ShopOrder databaseShopOrder = shopOrderRepository.findById(orderId).orElseThrow();
                    final List<ShopOrderDetailResponse> allOrderDetailResponse = shopOrderRepository.findAllByIdAndShopIdAndDate(
                        orderId,
                        shopId,
                        date,
                        true
                    );

                    final ShopSalesRefund databaseShopSalesRefund = shopSalesRefundRepository
                        .findByShopIdAndDateAndOrderId(shopId, date, orderId)
                        .orElseThrow();
                    final int detailSumPrice = allOrderDetailResponse.stream().mapToInt(ShopOrderDetailResponse::getPrice).sum();
                    assertThat(databaseShopOrder.getTotalPrice()).isEqualTo(detailSumPrice);
                    assertThat(databaseShopOrder.getDiscountPrice()).isEqualTo(0);
                    assertThat(
                        databaseShopSalesRefund.getShopPrice() +
                        databaseShopSalesRefund.getModelPrice() +
                        databaseShopSalesRefund.getMamaPrice()
                    ).isEqualTo(databaseShopOrder.getRefundPrice());
                    assertThat(shopPrice + modelPrice + mamaPrice).isEqualTo(databaseShopOrder.getRefundPrice());
                }
            }
        }
    }
}
