
package org.amv.trafficsoft.rest.xfcd.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.ImmutableList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * A data node corresponds to an entry in the imxfcd_node table with the
 * corresponding entries from the imxfcd_state and imxfcd_xfcd tables for the
 * corresponding CAN and STATE parameters.
 *
 * @author <a href='mailto:elisabeth.rosemann@amv-networks.com'>Elisabeth
 *         Rosemann</a>
 * @version $Revision: 3582 $
 * @since 13.06.2016
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "One data node for a vehicle - the current position and all parameters the vehicle sends for the current contract.")
public class NodeDto {
    @ApiModelProperty(notes = "Required. The ID of the data node for getData(), the vehicle ID for getLastData().", required = true)
    private long id;

    @ApiModelProperty(notes = "Required. The time the data was generated.", required = true)
    private Date timestamp;

    @ApiModelProperty(notes = "Optional. The longitude in decimal degree.")
    private BigDecimal longitude;

    @ApiModelProperty(notes = "Optional. The latitude in decimal degree.")
    private BigDecimal latitude;

    @ApiModelProperty(notes = "Optional. The vehicle speed in km/h.")
    private BigDecimal speed;

    @ApiModelProperty(notes = "Optional. The direction the car is heading in angular degree.")
    private BigDecimal heading;

    @ApiModelProperty(notes = "Optional. The altitude of the car in meters above sea level.")
    private BigDecimal altitude;

    @ApiModelProperty(notes = "Optional. The number of satellites currently in reach.")
    private Integer satellites;

    @ApiModelProperty(notes = "Optional. The horizontal dilution of precision.")
    private BigDecimal hdop;

    @ApiModelProperty(notes = "Optional. The vertical dilution of precision.")
    private BigDecimal vdop;

    @ApiModelProperty(notes = "Optional. A list of XFCD data (e.g. kmrd, speed, or any other Trafficsoft CAN parameter) received from the car.")
    private List<ParameterDto> xfcds = Collections.emptyList();

    @ApiModelProperty(notes = "Optional. A list of state parameters (e.g. vbat, move, or any other Trafficsoft State parameter) received from the car.")
    private List<ParameterDto> states = Collections.emptyList();

    public Date getTimestamp() {
        return Optional.ofNullable(timestamp)
                .map(Date::getTime)
                .map(Date::new)
                .orElse(null);
    }

    public void setTimestamp(Date timestamp) {
        Optional.ofNullable(timestamp)
                .map(Date::getTime)
                .map(Date::new)
                .ifPresent(date -> this.timestamp = date);
    }

    public List<ParameterDto> getXfcds() {
        return ImmutableList.copyOf(this.xfcds);
    }

    public void setXfcds(List<ParameterDto> xfcds) {
        this.xfcds = ImmutableList.copyOf(xfcds);
    }

    public List<ParameterDto> getStates() {
        return ImmutableList.copyOf(this.states);
    }

    public void setStates(List<ParameterDto> states) {
        this.states = ImmutableList.copyOf(states);
    }
}